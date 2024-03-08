package net.fze.ext.report;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/**
 * @author jarrysix
 */
public class SAXConfigItemParser {
    private static final SAXParserFactory FACTORY = SAXParserFactory.newInstance();

    static class Handler extends DefaultHandler {
        private String value = "";

        private ItemConfig cfg = new ItemConfig();

        public ItemConfig getItem() {
            return this.cfg;
        }

        @Override
        public void startElement (String uri, String localName,
                                  String qName, Attributes attributes)
                throws SAXException
        {
            super.startElement(uri,localName,qName,attributes);
            // https://blog.csdn.net/cmoaciopm/article/details/6889194
            // 处理CDATA的问题
            value = ""; // clear data,
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            super.endElement(uri, localName, qName);
            // 替换<!-- -->注释
            value = value.replaceAll("\\u003c!--(.+?)--\\u003e","");
            switch (qName) {
                case "ColumnMapping":
                    cfg.setColumnMapping(value);
                    break;
                case "Query":
                    cfg.setQuerySQL(value);
                    break;
                case "Import":
                    cfg.setImportSQL(value);
                    break;
                case "Total":
                    cfg.setTotalSQL(value);
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            super.characters(ch, start, length);
            String str = new String(ch, start, length);
            str = str.replaceAll("\\n", "").trim();
            // 处理CDATA的问题,故需要将数据合并
            value += str;
        }
    }

    public static ItemConfig parse(InputStream stream) throws Exception {
        SAXParser saxParser = FACTORY.newSAXParser();
        Handler handler = new Handler();
        saxParser.parse(stream, handler);
        return handler.getItem();
    }
}
