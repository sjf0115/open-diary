package com.sjf.open.hive.udf.udtf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaosi on 16-12-15.
 */
public class UrlTupleParseUDTF extends GenericUDTF{

    enum PARTNAME {
        HOST, PATH, QUERY, REF, PROTOCOL, AUTHORITY, FILE, USERINFO, QUERY_WITH_KEY, NULLNAME
    };


    int numCols;    // number of output columns
    String[] paths; // array of pathnames, each of which corresponds to a column
    PARTNAME[] partnames; // mapping from pathnames to enum PARTNAME
    Text[] retCols; // array of returned column values
    Text[] cols;    // object pool of non-null Text, avoid creating objects all the time
    Object[] nullCols; // array of null column values
    ObjectInspector[] inputOIs; // input ObjectInspectors
    boolean pathParsed = false;
    boolean seenErrors = false;
    URL url = null;
    Pattern p = null;
    String lastKey = null;

    @Override
    public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        inputOIs = args;
        numCols = args.length - 1;

        if (numCols < 1) {
            throw new UDFArgumentException("parse_url_tuple() takes at least two arguments: " +
                    "the url string and a part name");
        }

        for (int i = 0; i < args.length; ++i) {
            if (args[i].getCategory() != ObjectInspector.Category.PRIMITIVE ||
                    !args[i].getTypeName().equals(Constants.STRING_TYPE_NAME)) {
                throw new UDFArgumentException("parse_url_tuple()'s arguments have to be string type");
            }
        }

        seenErrors = false;
        pathParsed = false;
        url = null;
        p = null;
        lastKey = null;
        paths = new String[numCols];
        partnames = new PARTNAME[numCols];
        cols = new Text[numCols];
        retCols = new Text[numCols];
        nullCols = new Object[numCols];

        for (int i = 0; i < numCols; ++i) {
            cols[i] = new Text();
            retCols[i] = cols[i];
            nullCols[i] = null;
        }

        // construct output object inspector
        ArrayList<String> fieldNames = new ArrayList<String>(numCols);
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>(numCols);
        for (int i = 0; i < numCols; ++i) {
            // column name can be anything since it will be named by UDTF as clause
            fieldNames.add("c" + i);
            // all returned type will be Text
            fieldOIs.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        }

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    @Override
    public void process(Object[] args) throws HiveException {
        if (args[0] == null) {
            forward(nullCols);
            return;
        }
        // get the path names for the 1st row only
        if (!pathParsed) {
            for (int i = 0;i < numCols; ++i) {
                paths[i] = ((StringObjectInspector) inputOIs[i+1]).getPrimitiveJavaObject(args[i+1]);

                if (paths[i] == null) {
                    partnames[i] = PARTNAME.NULLNAME;
                } else if (paths[i].equals("HOST")) {
                    partnames[i] = PARTNAME.HOST;
                } else if (paths[i].equals("PATH")) {
                    partnames[i] = PARTNAME.PATH;
                } else if (paths[i].equals("QUERY")) {
                    partnames[i] = PARTNAME.QUERY;
                } else if (paths[i].equals("REF")) {
                    partnames[i] = PARTNAME.REF;
                } else if (paths[i].equals("PROTOCOL")) {
                    partnames[i] = PARTNAME.PROTOCOL;
                } else if (paths[i].equals("FILE")) {
                    partnames[i] = PARTNAME.FILE;
                } else if (paths[i].equals("AUTHORITY")) {
                    partnames[i] = PARTNAME.AUTHORITY;
                } else if (paths[i].equals("USERINFO")) {
                    partnames[i] = PARTNAME.USERINFO;
                } else if (paths[i].startsWith("QUERY:")) {
                    partnames[i] = PARTNAME.QUERY_WITH_KEY;
                    paths[i] = paths[i].substring(6); // update paths[i], e.g., from "QUERY:id" to "id"
                } else {
                    partnames[i] = PARTNAME.NULLNAME;
                }
            }
            pathParsed = true;
        }

        String urlStr = ((StringObjectInspector) inputOIs[0]).getPrimitiveJavaObject(args[0]);
        if (urlStr == null) {
            forward(nullCols);
            return;
        }

        try {
            String ret = null;
            url = new URL(urlStr);
            for (int i = 0; i < numCols; ++i) {
                ret = evaluate(url, i);
                if (ret == null) {
                    retCols[i] = null;
                } else {
                    if (retCols[i] == null) {
                        retCols[i] = cols[i]; // use the object pool rather than creating a new object
                    }
                    retCols[i].set(ret);
                }
            }

            forward(retCols);
            return;
        } catch (MalformedURLException e) {
            // parsing error, invalid url string
            if (!seenErrors) {
                seenErrors = true;
            }
            forward(nullCols);
            return;
        }
    }

    private String evaluate(URL url, int index) {
        if (url == null || index < 0 || index >= partnames.length)
            return null;

        switch (partnames[index]) {
            case HOST          : return url.getHost();
            case PATH          : return url.getPath();
            case QUERY         : return url.getQuery();
            case REF           : return url.getRef();
            case PROTOCOL      : return url.getProtocol();
            case FILE          : return url.getFile();
            case AUTHORITY     : return url.getAuthority();
            case USERINFO      : return url.getUserInfo();
            case QUERY_WITH_KEY: return evaluateQuery(url.getQuery(), paths[index]);
            case NULLNAME:
            default            : return null;
        }
    }

    private String evaluateQuery(String query, String key) {
        if (query == null || key == null) {
            return null;
        }

        if (!key.equals(lastKey)) {
            p = Pattern.compile("(&|^)" + key + "=([^&]*)");
        }

        lastKey = key;
        Matcher m = p.matcher(query);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    @Override
    public void close() throws HiveException {

    }
}
