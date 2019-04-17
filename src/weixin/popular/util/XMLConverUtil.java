package weixin.popular.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

/**
 * XML 数据接收对象转换工具类
 * @author LiYi
 *
 */
public class XMLConverUtil{

	private static Map<Class<?>,Unmarshaller> uMap = new HashMap<Class<?>,Unmarshaller>();
	private static Map<Class<?>,Marshaller> mMap = new HashMap<Class<?>,Marshaller>();

	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param xml
	 * @return
	 */
	public static <T> T convertToObject(Class<T> clazz,String xml){
		return convertToObject(clazz,new StringReader(xml));
	}

	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param inputStream
	 * @return
	 */
	public static <T> T convertToObject(Class<T> clazz,InputStream inputStream){
		return convertToObject(clazz,new InputStreamReader(inputStream));
	}

	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param reader
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static <T> T convertToObject(Class<T> clazz,Reader reader){
//		try {
//			if(!uMap.containsKey(clazz)){
//				JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
//				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//				uMap.put(clazz,unmarshaller);
//			}
//			return (T)uMap.get(clazz).unmarshal(reader);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	

	/*解决XXE漏洞 2018年9月30日17:03:46*/
	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param reader
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertToObject(Class<T> clazz,Reader reader){
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();

			spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
			spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			
			
			Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(), new InputSource(reader));

			
			if(!uMap.containsKey(clazz)){
				JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				uMap.put(clazz,unmarshaller);
			}
			return (T)uMap.get(clazz).unmarshal(xmlSource);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		catch (SAXNotRecognizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Object to XML
	 * @param object
	 * @return
	 */
	public static String convertToXML(Object object){
		try {
			if(!mMap.containsKey(object.getClass())){
				JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
	                public void escape(char[] ac, int i, int j, boolean flag,Writer writer) throws IOException {
	                writer.write( ac, i, j ); }
	            });
				mMap.put(object.getClass(), marshaller);
			}
			StringWriter stringWriter = new StringWriter();
			mMap.get(object.getClass()).marshal(object,stringWriter);
			return stringWriter.getBuffer().toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
