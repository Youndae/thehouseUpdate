package com.bit.house.recommenderProcess;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class RecommendProcess {

    private static Logger log = LoggerFactory.getLogger(RecommendProcess.class);

    public Collection<String> recommender(String productNo) {
        Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel("src/main/webapp/trainingFile/trainingFile.txt");

        Collection<String> recommendItems = word2Vec.wordsNearestSum(productNo, 4);
        log.info("Closest words : " + recommendItems);

        return recommendItems;
    }
}
