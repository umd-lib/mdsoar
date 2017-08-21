<?xml version="1.0" encoding="UTF-8"?>
<!--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

-->
<!--
    TODO: Describe this XSL file
    Author: Alexey Maslov

-->

<xsl:stylesheet xmlns:i18n="http://apache.org/cocoon/i18n/2.1"
                xmlns:dri="http://di.tamu.edu/DRI/1.0/"
                xmlns:mets="http://www.loc.gov/METS/"
                xmlns:xlink="http://www.w3.org/TR/xlink/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:dim="http://www.dspace.org/xmlns/dspace/dim"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:mods="http://www.loc.gov/mods/v3"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="i18n dri mets xlink xsl dim xhtml mods dc">

    <!-- Like the header, the footer contains various miscellaneous text, links, and image placeholders -->
    <xsl:template name="buildFooter">
        <footer>
        	 <div class="row">
                    <div class="col-xs-12 col-sm-12 community_footer">
                        <div>
                            <img src="{$theme-path}/images/community_logo.png" class="community_footer_logo" />
                            <p>
                                <br/>
                                Beneficial-Hodson Library<br/>
                                Hood College<br/>
                                401 Rosemont Avenue<br/>
                                Frederick, MD 21701<br/>
                                <a href="http://www.hood.edu">www.hood.edu</a>
                            </p>
                            <hr/>
                            <p>
                                If you wish to submit a copyright complaint or withdrawal request, please email 
                                <a href="mailto:mdsoar-help@umd.edu?subject=Takedown%20Request&amp;body=In%20your%20request,%20please%20include%20a%20link%20to%20the%20resource">mdsoar-help@umd.edu</a>.
                            </p>
                        </div>
                    </div>
                </div>
                <!--Invisible link to HTML sitemap (for search engines) -->
                <a class="hidden">
                    <xsl:attribute name="href">
                        <xsl:value-of
                                select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>
                        <xsl:text>/htmlmap</xsl:text>
                    </xsl:attribute>
                    <xsl:text>&#160;</xsl:text>
                </a>
            <p>&#160;</p>
        </footer>
    </xsl:template>

</xsl:stylesheet>