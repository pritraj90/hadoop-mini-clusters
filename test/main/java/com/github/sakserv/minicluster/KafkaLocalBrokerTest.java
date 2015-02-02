/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.sakserv.minicluster;

import com.github.sakserv.datetime.GenerateRandomDay;
import com.github.sakserv.kafka.KafkaTestConsumer;
import com.github.sakserv.kafka.KafkaTestProducer;
import com.github.sakserv.minicluster.config.ConfigVars;
import com.github.sakserv.minicluster.config.PropertyParser;
import com.github.sakserv.minicluster.impl.KafkaLocalBroker;
import com.github.sakserv.minicluster.impl.ZookeeperLocalCluster;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.common.ErrorMapping;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndOffset;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.hadoop.hdfs.DFSClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KafkaLocalBrokerTest {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(KafkaLocalBrokerTest.class);

    // Setup the property parser
    private static PropertyParser propertyParser;
    static {
        try {
            propertyParser = new PropertyParser(ConfigVars.DEFAULT_PROPS_FILE);
        } catch(IOException e) {
            LOG.error("Unable to load property file: " + propertyParser.getProperty(ConfigVars.DEFAULT_PROPS_FILE));
        }
    }

    private static ZookeeperLocalCluster zookeeperLocalCluster;
    private static KafkaLocalBroker kafkaLocalBroker;
    
    @BeforeClass
    public static void setUp() {
        kafkaLocalBroker = new KafkaLocalBroker.Builder()
                .setKafkaHostname(propertyParser.getProperty(ConfigVars.KAFKA_HOSTNAME_KEY))
                .setKafkaPort(Integer.parseInt(propertyParser.getProperty(ConfigVars.KAFKA_PORT_KEY)))
                .setKafkaBrokerId(Integer.parseInt(propertyParser.getProperty(ConfigVars.KAFKA_TEST_BROKER_ID_KEY)))
                .setKafkaProperties(new Properties())
                .setKafkaTempDir(propertyParser.getProperty(ConfigVars.KAFKA_TEST_TEMP_DIR_KEY))
                .setZookeeperConnectionString(propertyParser.getProperty(ConfigVars.ZOOKEEPER_CONNECTION_STRING_KEY))
                .build();
    }

    @Test
    public void testKafkaHostname() {
        assertEquals(propertyParser.getProperty(ConfigVars.KAFKA_HOSTNAME_KEY), kafkaLocalBroker.getKafkaHostname());
    }

    @Test
    public void testKafkaPort() {
        assertEquals(Integer.parseInt(propertyParser.getProperty(ConfigVars.KAFKA_PORT_KEY)), 
                (int) kafkaLocalBroker.getKafkaPort());
    }

    @Test
    public void testKafkaBrokerId() {
        assertEquals(Integer.parseInt(propertyParser.getProperty(ConfigVars.KAFKA_TEST_BROKER_ID_KEY)),
                (int) kafkaLocalBroker.getKafkaBrokerId());
    }

    @Test
    public void testKafkaProperties() {
        assertTrue(kafkaLocalBroker.getKafkaProperties() instanceof java.util.Properties);
    }

    @Test
    public void testKafkaTempDir() {
        assertEquals(propertyParser.getProperty(ConfigVars.KAFKA_TEST_TEMP_DIR_KEY), 
                kafkaLocalBroker.getKafkaTempDir());
    }

    @Test
    public void testZookeeperConnectionString() {
        assertEquals(propertyParser.getProperty(ConfigVars.ZOOKEEPER_CONNECTION_STRING_KEY),
                kafkaLocalBroker.getZookeeperConnectionString());
    }

}
