package net.reeuwijk.qlik.translate.util;

import java.util.Map;
import java.util.logging.Logger;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;

public class StringAnalyzer {

	private static StringAnalyzer instance;
	private static com.google.cloud.language.v1.LanguageServiceClient language;
	static final Logger logger = Logger.getLogger(StringAnalyzer.class.getName());

	private StringAnalyzer() throws Exception {

	}

	static {
		try {
			instance = new StringAnalyzer();
			language = LanguageServiceClient.create();
		} catch (Exception e) {
			throw new RuntimeException("Exception occured in creating StringAnalyzer" + e.getMessage());
		}
	}

	public static StringAnalyzer getInstance() {
		return instance;
	}

	public String analyzeEntities(String strData) {
		Document doc = Document.newBuilder().setContent(strData).setType(Type.PLAIN_TEXT).build();
		AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder().setDocument(doc)
				.setEncodingType(EncodingType.UTF16).build();
		StringBuffer str = new StringBuffer();

		AnalyzeEntitiesResponse response = language.analyzeEntities(request);

		int entityLen = response.getEntitiesList().size();
		for (Entity entity : response.getEntitiesList()) {
			str.append(entity.getName()).append(";");
			str.append(entity.getType()).append(";");
			str.append(entity.getSalience()).append(";");
			int metadataLen = entity.getMetadataMap().entrySet().size();
			for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
				str.append(entry.getKey()).append(":");
				str.append(entry.getValue());
				if(--metadataLen > 0) str.append(",");
			}
			str.append((--entityLen > 0) ? "|" : "");
//			for (EntityMention mention : entity.getMentionsList()) {
//				System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
//				System.out.printf("Content: %s\n", mention.getText().getContent());
//				System.out.printf("Type: %s\n\n", mention.getType());
//			}
		}
//		logger.warning(str.toString());
		return str.toString();
	}

}