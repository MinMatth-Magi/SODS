package com.github.miachm.sods;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;

class XmlReaderEventImpl implements XmlReader {
    private static XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    private XMLStreamReader reader = null;

    @Override
    public XmlReaderInstanceEventImpl load(InputStream in) throws IOException {
        try {
            // #77 Make the ODS files with DTD work on the JBoss-bundled Woodstox,
            //     or ensure they work on another StAX implementation set to validate by default.
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);

            reader = inputFactory.createXMLStreamReader(in);
            // Skip start of document
            try {
                reader.next();
            }
            catch (XMLStreamException e)
            {
                // Empty file, skipping
                reader.close();
                return null;
            }
            return new XmlReaderInstanceEventImpl(reader, null);
        } catch (XMLStreamException e) {
            throw new NotAnOdsException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new NotAnOdsException(e);
        }
    }
}
