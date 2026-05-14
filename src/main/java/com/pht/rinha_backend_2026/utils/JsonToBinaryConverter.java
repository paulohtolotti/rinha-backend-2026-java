package com.pht.rinha_backend_2026.utils;

import com.pht.rinha_backend_2026.constants.DatasetConfig;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.json.JsonFactory;

import java.io.*;

public class JsonToBinaryConverter {

    public void convert(String jsonInputPath, String vectorOutputPath, String labelOutputPath) throws IOException {

            JsonFactory factory = new JsonFactory();

            try(JsonParser parser = factory.createParser(new FileInputStream(jsonInputPath));
                DataOutputStream vectorOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(vectorOutputPath))
                );
                DataOutputStream labelOutput = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(labelOutputPath))
                )
            ) {
            parser.nextToken();
            int i = 0, j = 0;
            while(parser.nextToken() != JsonToken.END_ARRAY) {
                i++;
                readRecord(parser, vectorOut, labelOutput);
                if(i % 10 == 0) {
                    j++;
                }
            }
        }
    }

    private void readRecord(JsonParser parser, DataOutputStream vectorOut, DataOutputStream labelOut) throws IOException {

        while(parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.currentName();
            parser.nextToken();

            if("vector".equals(fieldName)) {
                readVector(parser, vectorOut);
            }
            else if("label".equals(fieldName)) {
                readLabel(parser, labelOut);
            } else {
                parser.skipChildren();
            }
        }
    }

    private void readVector(JsonParser parser, DataOutputStream vectorOut) throws IOException {
        while(parser.nextToken() != JsonToken.END_ARRAY) {
            vectorOut.writeByte((int) (127 * parser.getFloatValue()));
        }
    }

    private void readLabel (JsonParser parser, DataOutputStream labelOut) throws IOException {
        String label = parser.getValueAsString();

        if("legit".equals(label)) {
            labelOut.writeByte(DatasetConfig.LABEL_LEGIT);
        }
        else if("fraud".equals(label)) {
            labelOut.writeByte(DatasetConfig.LABEL_FRAUD);
        }
    }
}
