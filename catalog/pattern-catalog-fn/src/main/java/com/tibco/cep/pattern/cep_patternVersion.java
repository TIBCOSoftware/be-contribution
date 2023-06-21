/*
 * $HeadURL: https://svn.tibco.com/svn/be/trunk/be-main/runtime/modules/pattern/pattern-be/src/com/tibco/cep/pattern/cep_patternVersion.tag $ $Revision: 48199 $ $Date: 2012-02-16 22:27:01 -0500 (Thu, 16 Feb 2012) $
 *
 * Copyright(c) 2004-2012 TIBCO Software Inc. All rights reserved.
 *
 * cep-pattern.jar Version Information
 *
 */

/*





        AUTOMATICALLY GENERATED AT BUILD TIME !!!!

        DO NOT EDIT !!!





 * "cep_patternVersion.java" is automatically generated at
 * build time from "cep_patternVersion.tag"
 *
 * Any maintenance changes MUST be applied to "cep_patternVersion.tag"
 * and an official build triggered to propagate such changes to
 * "cep_patternVersion.java"
 *
 * If maintenance changes must be applied immediately without going
 * through an official build, then they MUST be applied to *BOTH*
 * "cep_patternVersion.tag" *AND* "cep_patternVersion.java"
 *
 */

package com.tibco.cep.pattern;

public final class cep_patternVersion {
        static final public String asterisks       = "**********************************************************************";
        static final public String copyright       = "Copyright(c) 2004-2023 Cloud Software Group, Inc. All rights reserved.";
        static final public String line_separator  = System.getProperty("line.separator");
        static final public String version         = "6.3.0";
        static final public String engineMinVersion = "6.3.0.0";
        static final public String engineName      = "be-engine";
        static final public String build           = "105";
        static final public String buildDate       = "2023-06-06";
        static final public String container_id    = "be-engine";
        static final private String company        = "Cloud Software Group, Inc.";
        static final private String component      = "TIBCO BusinessEvents";
        static final private String license        = "*** !!! NOT FOR PRODUCTION USE !!! ***";

        static public String getVersion() {
                return "Version " + version + "." + build + ", " + buildDate;
        }

        static public String getCompany() {
                return company;
        }

        static public String getComponent() {
                return component;
        }

        static public void main(String[] args) {
                System.out.println(getCompany() + " - " + getComponent() + " " + getVersion() + " " + getLicense());
        }

        static public String getLicense() {
                return license;
        }
}
