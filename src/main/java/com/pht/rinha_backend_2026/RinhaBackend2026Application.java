package com.pht.rinha_backend_2026;

import com.pht.rinha_backend_2026.utils.JsonToBinaryConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class RinhaBackend2026Application {

	public static void main(String[] args) throws IOException {
		// Pré-processamento da aplicação
		String datasetPath = "src/main/resources/static/references.json";
		String vectorOut = "src/main/resources/static/vectors.f32";
		String labelOut = "src/main/resources/static/labels.u8";

		JsonToBinaryConverter converter = new JsonToBinaryConverter();

		converter.convert(datasetPath, vectorOut, labelOut);
		SpringApplication.run(RinhaBackend2026Application.class, args);
	}

}
