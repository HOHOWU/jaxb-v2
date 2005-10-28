package com.sun.xml.bind.v2.schemagen;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.namespace.QName;

import com.sun.xml.bind.v2.schemagen.xmlschema.LocalElement;
import com.sun.xml.bind.v2.schemagen.xmlschema.Schema;
import com.sun.xml.bind.v2.schemagen.xmlschema.LocalAttribute;
import com.sun.xml.txw2.TypedXmlWriter;

/**
 * Represents the form default value.
 *
 * @author Kohsuke Kawaguchi
 */
enum Form {
    QUALIFIED(XmlNsForm.QUALIFIED) {
        void declare(String attName,Schema schema) {
            schema._attribute(attName,"qualified");
        }
    },
    UNQUALIFIED(XmlNsForm.UNQUALIFIED) {
        void declare(String attName,Schema schema) {
            // pointless, but required by the spec.
            // people need to understand that @attributeFormDefault is a syntax sugar
            schema._attribute(attName,"unqualified");
        }
    },
    UNSET(XmlNsForm.UNSET) {
        void declare(String attName,Schema schema) {
        }
    };

    /**
     * The same constant defined in the spec.
     */
    private final XmlNsForm xnf;

    Form(XmlNsForm xnf) {
        this.xnf = xnf;
    }

    /**
     * Writes the attribute on the generated &lt;schema> element.
     */
    abstract void declare(String attName, Schema schema);

    /**
     * Given the effective 'form' value, write (or suppress) the @form attribute
     * on the generated XML.
     */
    public void writeForm(LocalElement e, QName tagName) {
        _writeForm(e,tagName);
    }

    public void writeForm(LocalAttribute a, QName tagName) {
        _writeForm(a,tagName);
    }

    private void _writeForm(TypedXmlWriter e, QName tagName) {
        boolean qualified = tagName.getNamespaceURI().length()>0;

        if(qualified && this!=QUALIFIED)
            e._attribute("form","qualified");
        else
        if(!qualified && this==QUALIFIED)
            e._attribute("form","unqualified");
    }

    /**
     * Gets the constant the corresponds to the given {@link XmlNsForm}.
     */
    public static Form get(XmlNsForm xnf) {
        for (Form v : values()) {
            if(v.xnf==xnf)
                return v;
        }
        throw new IllegalArgumentException();
    }

}
