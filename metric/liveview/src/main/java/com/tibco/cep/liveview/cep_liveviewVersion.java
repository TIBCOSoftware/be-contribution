/*
 * $HeadURL:  $ $Revision:  $ $Date: $
 *
 * Copyright(c) 2007-2012 TIBCO Software Inc. All rights reserved.
 *
 * cep-liveview.jar Version Information
 *
 */

/*





        AUTOMATICALLY GENERATED AT BUILD TIME !!!!

        DO NOT EDIT !!!





 * "cep_liveviewVersion.java" is automatically generated at
 * build time from "cep-liveviewVersion.tag"
 *
 * Any maintenance changes MUST be applied to "cep_liveviewVersion.tag"
 * and an official build triggered to propagate such changes to
 * "cep_liveviewVersion.java"
 *
 * If maintenance changes must be applied immediately without going
 * through an official build, then they MUST be applied to *BOTH*
 * "cep_liveviewVersion.tag" *AND* "cep_liveviewVersion.java"
 *
 */

package com.tibco.cep.liveview;

public final class cep_liveviewVersion {
        static final public String asterisks       = "**********************************************************************";
        static final public String copyright       = "Copyright(c) 2004-2023 Cloud Software Group, Inc. All rights reserved.";
        static final public String line_separator  = System.getProperty("line.separator");
        static final public String version         = "6.3.0";
        static final public String build           = "118";
        static final public String buildDate       = "2023-06-13";
        static final public String container_id    = "be-engine";
        static final private String company        = "Cloud Software Group, Inc.";
        static final private String component      = "TIBCO BusinessEvents";
        static final private String license        = "";

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
