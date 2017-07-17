package com.sjf.open.utils;

import com.google.common.collect.Lists;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.DependencyParseAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaosi on 17-7-15.
 */
public class NLPUtil {

    private static StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");

    private static Pattern pattern = Pattern.compile(".*?\\((.*?)-(.*?), (.*?)-(.*?)\\)");

    /**
     * 分词
     * @param content
     */
    public static void participle(String content){

        // 停用词
        String path = NLPUtil.class.getResource("/dict/").getPath().toString() + "stopwords.txt";
        List<String> stopDictList = FileUtil.read(path);

        Annotation annotation = new Annotation(content);
        pipeline.annotate(annotation);

        StringBuilder stringBuilder = new StringBuilder();
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if (stopDictList.contains(word)) {
                    continue;
                }
                stringBuilder.append(word + " ");
            }
        }

        System.out.println(stringBuilder);
    }

    public static void syntaxAnalysis(String content){

        String[] lineArray = content.split("[，。！?]");
        if (lineArray == null || lineArray.length <= 0){
            return;
        }

        for (String subLine : lineArray) {

            if (subLine == null || subLine.equals("")){
                continue;
            }

            Annotation document = new Annotation(subLine);
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class);
                System.out.println(dependencies);
                List<String> list = extractLabels(dependencies.toList());
                System.out.println("Result-----------"+list);
            }
        }
    }

    public static List<String> extractLabels(String dependencyComments) {
        String[] dependencyArray = dependencyComments.split("\n");
        int len = dependencyArray.length;
        List<String> labelList = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            if (dependencyArray[i].contains("nsubj")) {
                // 处理主副修饰词与主修饰词的情况
                if (i < len - 2 && dependencyArray[i + 1].contains("advmod")
                        && dependencyArray[i + 2].contains("advmod")) {
                    // 1.处理nsubj + advmod + advmod
                    Set<String> wordsSet = new HashSet<>();// words用于判断该词是否已经添加进去
                    List<String[]> wordsList = new ArrayList<>();
                    for (int j = 0; j <= 2; j++) {
                        List<String[]> tempList = getWordsUnit(dependencyArray[i + j]);
                        if (tempList != null) {
                            for (int k = 0; k < 2; k++) {
                                String item = tempList.get(k)[0];
                                if (!wordsSet.contains(item)) {
                                    wordsList.add(tempList.get(k));
                                    wordsSet.add(item);
                                }
                            }

                        }

                    }
                    if (wordsList.size() > 0) {
                        sortList(wordsList);
                        String ss = joinList(wordsList);
                        labelList.add(ss);
                    }
                    i += 2;
                } else if (i < len - 1 && dependencyArray[i + 1].contains("advmod")) {
                    // 2.处理nsubj + advmod
                    Set<String> wordsSet = new HashSet<>();// words用于判断该词是否已经添加进去
                    List<String[]> wordsList = new ArrayList<>();
                    for (int j = 0; j <= 1; j++) {
                        List<String[]> tempList = getWordsUnit(dependencyArray[i + j]);
                        if (tempList != null) {
                            for (int k = 0; k < 2; k++) {
                                String item = tempList.get(k)[0];
                                if (!wordsSet.contains(item)) {
                                    wordsList.add(tempList.get(k));
                                    wordsSet.add(item);
                                }
                            }

                        }

                    }
                    if (wordsList.size() > 0) {
                        sortList(wordsList);
                        String ss = joinList(wordsList);
                        labelList.add(ss);
                    }
                    i++;
                }
            } else if (dependencyArray[i].contains("advmod")) {
                if (i < len - 1 && dependencyArray[i + 1].contains("advmod")) {
                    // 3.处理advmod + advmod
                    Set<String> wordsSet = new HashSet<>();// words用于判断该词是否已经添加进去
                    List<String[]> wordsList = new ArrayList<>();
                    for (int j = 0; j <= 1; j++) {
                        List<String[]> tempList = getWordsUnit(dependencyArray[i + j]);
                        if (tempList != null) {
                            for (int k = 0; k < 2; k++) {
                                String item = tempList.get(k)[0];
                                if (!wordsSet.contains(item)) {
                                    wordsList.add(tempList.get(k));
                                    wordsSet.add(item);
                                }
                            }

                        }

                    }
                    if (wordsList.size() > 0) {
                        sortList(wordsList);
                        String ss = joinList(wordsList);
                        labelList.add(ss);
                    }
                    i++;
                } else if (i < len - 1 && dependencyArray[i + 1].contains("amod")) {
                    // 4.处理advmod + amod
                    Set<String> wordsSet = new HashSet<>();// words用于判断该词是否已经添加进去
                    List<String[]> wordsList = new ArrayList<>();
                    for (int j = 0; j <= 1; j++) {
                        List<String[]> tempList = getWordsUnit(dependencyArray[i + j]);
                        if (tempList != null) {
                            for (int k = 0; k < 2; k++) {
                                String item = tempList.get(k)[0];
                                if (!wordsSet.contains(item)) {
                                    wordsList.add(tempList.get(k));
                                    wordsSet.add(item);
                                }
                            }

                        }

                    }
                    if (wordsList.size() > 0) {
                        sortList(wordsList);
                        String ss = joinList(wordsList);
                        labelList.add(ss);
                    }
                    i++;
                } else {
                    // 5.处理advmod
                    List<String[]> wordsList = new ArrayList<>();
                    List<String[]> tempList = getWordsUnit(dependencyArray[i]);
                    if (tempList != null) {
                        for (int k = 0; k < 2; k++) {
                            wordsList.add(tempList.get(k));
                        }
                    }
                    if (wordsList.size() > 0) {
                        sortList(wordsList);
                        String ss = joinList(wordsList);
                        labelList.add(ss);
                    }
                }
            }
        }

        return labelList;
    }


    /**
     * 解析依存二元关系
     *
     * @param line
     * @return
     */
    public static List<String[]> getWordsUnit(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            if (matcher.groupCount() == 4) {
                String[] words1 = new String[2];
                String[] words2 = new String[2];
                List<String[]> list = new ArrayList<>();
                words1[0] = matcher.group(1);
                words1[1] = matcher.group(2);
                words2[0] = matcher.group(3);
                words2[1] = matcher.group(4);
                list.add(words1);
                list.add(words2);
                return list;
            }
        }
        return null;
    }

    /**
     * 对list按照位置进行排序
     *
     * @param list
     */
    public static void sortList(List<String[]> list) {
        Collections.sort(list, new Comparator<String[]>() {

            @Override
            public int compare(String[] o1, String[] o2) {
                // TODO Auto-generated method stub
                return o1[1].compareTo(o2[1]);
            }
        });
    }

    /**
     * 将list中的字符串拼接起来
     *
     * @param list
     * @return
     */
    public static String joinList(List<String[]> list) {
        String line = "";
        for (String[] ss : list) {
            line += ss[0] + "\t||\t";
        }
        return line;
    }

    public static void main(String[] args) {
        String str = "衣服面料非常好";
        syntaxAnalysis(str);
    }
}
