package com.mapr.labs;

import org.nrf_arts.ixretail.namespace.ObjectFactory;
import org.nrf_arts.ixretail.namespace.POSLogRoot;

import javax.xml.bind.*;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by mlalapet on 11/3/15.
 */
public class POSXMLParser {

    public static String convertToXML(POSLogRoot root) throws JAXBException {
        // create jaxb context
        JAXBContext jaxbContext = JAXBContext.newInstance(POSLogRoot.class);

        // create jaxb marshaller
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // pretty print xml
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // marshal object
        StringWriter swriter = new StringWriter();
        jaxbMarshaller.marshal((new ObjectFactory()).createPOSLog(root), swriter);

        return swriter.toString();
    }

    public static POSLogRoot parseXML(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(POSLogRoot.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement returnObject = (JAXBElement)unmarshaller.unmarshal(new StringReader(xml));;
        POSLogRoot pos = (POSLogRoot) returnObject.getValue();

        return pos;
    }

}
