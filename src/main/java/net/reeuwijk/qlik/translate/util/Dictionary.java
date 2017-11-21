package net.reeuwijk.qlik.aai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.text.StringEscapeUtils;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class Dictionary {

	private static final String TRANSLATION_DIR = "TRANSLATION_DIR";
	private static final String ORIGIN_LANGUAGE = "ORIGIN_LANGUAGE";
	private static Dictionary instance = null;
	private HashMap<String, WordList> wordLists = new HashMap<String, WordList>();
	private Properties functionProperties = null;

	static final Logger logger = Logger.getLogger(Dictionary.class.getName());

	public class WordList {
		private String from = null;
		private String to = null;
		private Properties wordList = null;
		private File wordFile = null;
		private Translate translateApi = null;
		private TranslateOption fromOption = null;
		private TranslateOption toOption = null;

		public WordList(String fromLanguage, String toLanguage, String transDir) {
			translateApi = TranslateOptions.getDefaultInstance().getService();

			this.from = fromLanguage;
			this.fromOption = TranslateOption.sourceLanguage(fromLanguage);
			this.to = toLanguage;
			this.toOption = TranslateOption.targetLanguage(toLanguage);
			wordList = new Properties();
			wordFile = new File(transDir, this.from + "-" + this.to + ".properties");
			try {
				if (wordFile.exists()) {
					wordList.load(new FileInputStream(wordFile));
				} else {
					wordList.store(new FileOutputStream(wordFile), "Initial Store");
				}
			} catch (FileNotFoundException e) {
				logger.severe(e.getMessage());
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}

		public String translate(String text) {
			String returnValue = text;
			if (!to.equals(from)) {
				try {
					if (wordList.containsKey(text)) {
						returnValue = wordList.getProperty(text);
					} else {
						Translation translation = translateApi.translate(text, fromOption, toOption);					
						returnValue = StringEscapeUtils.unescapeHtml4(translation.getTranslatedText());
						wordList.setProperty(text, returnValue);
						wordList.store(new FileOutputStream(wordFile), "Added " + text);
					}
				} catch (FileNotFoundException e) {
					logger.severe(e.getMessage());
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}
			return returnValue;
		}
	}

	private void loadProperties() {
		functionProperties = new Properties();
		File p = new File("Dictionary.properties");
		try {
			if (p.exists()) {
				functionProperties.load(new FileInputStream(p));
			}
		} catch (FileNotFoundException e) {
			logger.severe(e.getMessage());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	protected Dictionary() {
		loadProperties();
	}

	public static Dictionary getInstance() {
		if (instance == null) {
			instance = new Dictionary();
		}
		return instance;
	}

	public String translate(String toLang, String text) {
		WordList wl = wordLists.get(toLang);
		if (wl == null) {
			wl = new WordList(functionProperties.getProperty(ORIGIN_LANGUAGE), toLang,
					functionProperties.getProperty(TRANSLATION_DIR));
			wordLists.put(toLang, wl);
		}
		return wl.translate(text);
	}

}
