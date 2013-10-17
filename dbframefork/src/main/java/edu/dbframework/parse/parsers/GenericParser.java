package edu.dbframework.parse.parsers;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 
 * @author GreenPoser
 * 
 */
public class GenericParser {

    private File parsingFile;
	
	public GenericParser(String fileName) {
        this.parsingFile = new File(fileName);
	}

	@SuppressWarnings("deprecation")
	public void addBeanToXML(Object bean) {
		try {
			BeanWriter writer = new BeanWriter(new FileOutputStream(parsingFile));
			writer.getXMLIntrospector().setAttributesForPrimitives(true);
			writer.enablePrettyPrint();
			writer.setWriteIDs(false);
			writer.write(bean);
		} catch (Exception e) {
			throw new RuntimeException("Exception in GenericParser.addBeanToXml: ", e);
		}
	}

	public Object getBeanFromXML(Class clazz) {
		Object bean = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(parsingFile);
			BeanReader beanReader = new BeanReader();
			beanReader.getXMLIntrospector().getConfiguration()
					.setAttributesForPrimitives(true);
			beanReader.getBindingConfiguration().setMapIDs(true);
			beanReader.registerBeanClass(clazz.getSimpleName(), clazz);
			bean = beanReader
					.parse(fileInputStream);
		} catch (Exception e) {
			throw new RuntimeException("Exception in GenericParser.getBeanFromXML: ", e);
		}
		return bean;
	}

    public File getParsingFile() {
        return parsingFile;
    }

    public void setParsingFile(String fileName) {
        parsingFile = new File(fileName);
    }
}
