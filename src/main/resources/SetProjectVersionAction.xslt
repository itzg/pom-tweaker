<xsl:transform version="1.0"
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:p="http://maven.apache.org/POM/4.0.0">

    <xsl:param name="version"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/p:project/p:version/text()">
        <xsl:value-of select="$version"/>
    </xsl:template>

</xsl:transform>