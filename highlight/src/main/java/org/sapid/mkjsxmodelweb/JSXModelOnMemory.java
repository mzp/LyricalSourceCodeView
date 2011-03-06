/*
 * Copyright (C) Sapid Project 2009-2010
 */
package org.sapid.mkjsxmodelweb;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.sapid.parser.common.ParseException;
import org.sapid.parser.jsxmodel.EcmaScript;
import org.sapid.parser.jsxmodel.JSXTGenerator;
import org.sapid.parser.jsxmodel.SimpleNode;
import org.sapid.parser.jsxmodel.TokenMgrError;
import org.sapid.parser.jsxmodel.XMLSourceGenerator;
import org.sapid.util.SpdEncoding;
import org.xml.sax.SAXException;

/**
 * JSX-model without file
 * @author mallowlabs
 */
public class JSXModelOnMemory {
    /** source code string */
    private String code = "";

    /** visitor */
    private JSXTGenerator visitor;

    /** generator */
    private XMLSourceGenerator xsg;

    /**
     * Constructor
     *
     * @param code JavaScript Source code
     * @throws FileNotFoundException
     */
    public JSXModelOnMemory(String code) throws FileNotFoundException {
        this.code = code;
        xsg = new XMLSourceGenerator(SpdEncoding.UTF8.toString(), System
                .getProperty("line.separator"));
    }

    /**
     * Get parse result
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public String getModelByString() throws ParserConfigurationException,
            SAXException, IOException {
        return xsg.generateSource(visitor.getRootNode());
    }

    /**
     * Parse
     * @throws IOException
     * @throws ParseException
     */
    public void analyze() throws IOException, ParseException {
        EcmaScript parser = new EcmaScript(new StringReader(code));
        try {
            SimpleNode node = parser.Program();
            visitor = new JSXTGenerator();
            node.jjtAccept(visitor, null);
        } catch (org.sapid.parser.jsxmodel.ParseException e) {
            throw new ParseException(e);
        } catch (TokenMgrError e) {
            throw new ParseException(e);
        } catch (Error e) {
            throw new ParseException(e);
        }
    }

    /**
     * @param args
     * @throws ParseException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void main(String[] args) throws IOException, ParseException,
            ParserConfigurationException, SAXException {
        JSXModelOnMemory model = new JSXModelOnMemory("var i = 0;");
        model.analyze();
        System.out.println(model.getModelByString());
    }

}
