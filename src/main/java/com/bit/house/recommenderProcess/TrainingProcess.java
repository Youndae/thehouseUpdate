/*******************************************************************************
 * Copyright (c) 2015-2019 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/
package com.bit.house.recommenderProcess;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author raver119@gmail.com
 *
 * amended by hahyun
 */
public class TrainingProcess {

    private static Logger log = LoggerFactory.getLogger(TrainingProcess.class);

    public void training(List<String> sentences) {

        SentenceIterator iterator = new CollectionSentenceIterator(sentences);
        TokenizerFactory tokenizer = new DefaultTokenizerFactory();

        tokenizer.setTokenPreProcessor(new CommonPreprocessor() {
            //lib에 의해 자동으로 소문자, 숫자 제외처리된걸 해제한다.
            @Override
            public String preProcess(String token) {
                return StringCleaning.stripPunct(token).toUpperCase();
            }

        });

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(50)
                .layerSize(100)
                .seed(42)
                .windowSize(3)
                .tokenizerFactory(tokenizer)
                .iterate(iterator)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();


        Collection<String> words = vec.wordsNearest("DK013-100100001", 5);
        log.info(" Words closest to : {}", words);

        WordVectorSerializer.writeWord2VecModel(vec, new File("src/main/webapp/trainingFile/trainingFile.txt"));


        Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel("src/main/webapp/trainingFile/trainingFile.txt");

        /*PLEASE NOTE: after model is restored, it's still required to set SentenceIterator and TokenizerFactory, if you're going to train this model*/


        SentenceIterator uptrainIterator = new CollectionSentenceIterator(sentences);
        TokenizerFactory uptrainTokenizer = new DefaultTokenizerFactory();

        uptrainTokenizer.setTokenPreProcessor(new CommonPreprocessor() {
            @Override
            public String preProcess(String token) {
                return StringCleaning.stripPunct(token).toUpperCase();
            }


        });

        word2Vec.setTokenizerFactory(uptrainTokenizer);
        word2Vec.setSentenceIterator(uptrainIterator);


        log.info("Word2vec uptraining...");

        word2Vec.fit();

        Collection<String> uptrainWords = word2Vec.wordsNearestSum("UUIIOOPP4-300100002", 5);
        log.info("Closest words : " + uptrainWords);

        for(String word:uptrainWords){
            System.out.println(word);
        }


        /*
            Model can be saved for future use now
         */
    }
}
