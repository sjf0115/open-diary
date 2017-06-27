package com.sjf.open;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * Created by xiaosi on 17-6-26.
 */
public class Test {
    public static void main(String[] args) {
        StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");

        // 初始化Annotation
        Annotation annotation = new Annotation("已经很多次到这家农家院，室内卫生比较干净，但是使用热水的时候，需要放很长时间，老板家提供很多的图书，麻将，棋牌等，房子后面就是大山，距离幽谷神潭和天池（问一下老板小路）距离差不错，最重要的是老板家的食物特别美味，大家可以去尝一尝。");

        //
        pipeline.annotate(annotation);

        // 从Annotation中获取CoreMap List
        List<CoreMap> coreMapList = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        CoreMap coreMap = coreMapList.get(0);

        List<CoreLabel> tokens = coreMap.get(CoreAnnotations.TokensAnnotation.class);
        System.out.println("字/词" + "\t " + "词性" + "\t " + "实体标记");
        System.out.println("-----------------------------");
        for (CoreLabel token : tokens) {
            String word = token.getString(CoreAnnotations.TextAnnotation.class);
            String pos = token.getString(CoreAnnotations.PartOfSpeechAnnotation.class);
            String ner = token.getString(CoreAnnotations.NamedEntityTagAnnotation.class);
            String result = String.format("%-10s%-10s%-10s", word, pos, ner);
            System.out.println(result);
            //System.out.println(word + "\t " + pos + "\t " + ner);
        }
    }
}
