package tfidf;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Test {

	public static void main(String[] args) throws IOException {
		StringReader reader = new StringReader("你好，世界，你是谁啊");
		IKSegmenter ikSegementer = new IKSegmenter(reader, true);
		
		Lexeme word = null;
		
		while((word=ikSegementer.next())!=null){
			String str = word.getLexemeText();
			System.out.println(str);
		}
	}

}
