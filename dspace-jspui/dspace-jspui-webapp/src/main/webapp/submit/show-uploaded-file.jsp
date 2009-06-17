<%--
  - show-uploaded-file.jsp
  -
  - Version: $Revision: 3705 $
  -
  - Date: $Date: 2009-04-11 19:02:24 +0200 (Sat, 11 Apr 2009) $
  -
  - Copyright (c) 2002, Hewlett-Packard Company and Massachusetts
  - Institute of Technology.  All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are
  - met:
  -
  - - Redistributions of source code must retain the above copyright
  - notice, this list of conditions and the following disclaimer.
  -
  - - Redistributions in binary form must reproduce the above copyright
  - notice, this list of conditions and the following disclaimer in the
  - documentation and/or other materials provided with the distribution.
  -
  - - Neither the name of the Hewlett-Packard Company nor the name of the
  - Massachusetts Institute of Technology nor the names of their
  - contributors may be used to endorse or promote products derived from
  - this software without specific prior written permission.
  -
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  - ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  - LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
  - A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
  - HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  - INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  - BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
  - OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  - ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
  - TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
  - USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
  - DAMAGE.
  --%>

<%--
  - Show uploaded file (single-file submission mode)
  -
  - Attributes to pass in
  -    just.uploaded    - Boolean indicating whether the user has just
  -                       uploaded a file OK
  -    show.checksums   - Boolean indicating whether to show checksums
  -
  - FIXME: Merely iterates through bundles, treating all bit-streams as
  -        separate documents.  Shouldn't be a problem for early adopters.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
    
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

    boolean justUploaded = ((Boolean) request.getAttribute("just.uploaded")).booleanValue();
    boolean showChecksums = ((Boolean) request.getAttribute("show.checksums")).booleanValue();

    // Get the bitstream
    Bitstream[] all = subInfo.getSubmissionItem().getItem().getNonInternalBitstreams();
    Bitstream bitstream = all[0];
    BitstreamFormat format = bitstream.getFormat();
%>


<dspace:layout locbar="off"
               navbar="off"
               titlekey="jsp.submit.show-uploaded-file.title"
               nocache="true">

    <form action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">

        <jsp:include page="/submit/progressbar.jsp"/>

<%
    if (justUploaded)
    {
%>
        <%-- <h1>Submit: File Uploaded Successfully</h1> --%>
		<h1><fmt:message key="jsp.submit.show-uploaded-file.heading1"/></h1>

        <%-- <p><strong>Your file was successfully uploaded.</strong></p> --%>
		<p><strong><fmt:message key="jsp.submit.show-uploaded-file.info1"/></strong></p>
<%
    }
    else
    {
%>
        <%-- <h1>Submit: Uploaded File</h1> --%>
		<h1><fmt:message key="jsp.submit.show-uploaded-file.heading2"/></h1>
<%
    }
%>
        <%-- <p>Here are the details of the file you have uploaded.  Please check the
        details before going to the next step.
        &nbsp;&nbsp;&nbsp;<dspace:popup page="/help/index.html#uploadedfile">(More Help...)</dspace:popup></p> --%>

		<div><fmt:message key="jsp.submit.show-uploaded-file.info2"/>
        &nbsp;&nbsp;&nbsp;<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\")+ \"#uploadedfile\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></div>

        <table class="miscTable" align="center">
            <tr>
                <%-- <th class="oddRowOddCol">File</th>
                <th class="oddRowEvenCol">Size</th>
                <th class="oddRowOddCol">File Format</th> --%>
                
				<th id="t1" class="oddRowOddCol"><fmt:message key="jsp.submit.show-uploaded-file.file"/></th>
                <th id="t2" class="oddRowEvenCol"><fmt:message key="jsp.submit.show-uploaded-file.size"/></th>
                <th id="t3" class="oddRowOddCol"><fmt:message key="jsp.submit.show-uploaded-file.format"/></th>

<%
    if (showChecksums)
    {
%>
                <%-- <th class="oddRowEvenCol">Checksum</th> --%>
				<th id="t4" class="oddRowEvenCol"><fmt:message key="jsp.submit.show-uploaded-file.checksum"/></th>
<%
    }
%>
            </tr>
            <tr>
                <td headers="t1" class="evenRowOddCol"><a href="<%= request.getContextPath() %>/retrieve/<%= bitstream.getID() %>/<%= org.dspace.app.webui.util.UIUtil.encodeBitstreamName(bitstream.getName()) %>" target="_blank"><%= bitstream.getName() %></a></td>
                <td headers="t2" class="evenRowEvenCol"><fmt:message key="jsp.submit.show-uploaded-file.size-in-bytes">
                    <fmt:param><fmt:formatNumber><%= bitstream.getSize() %></fmt:formatNumber></fmt:param>
                </fmt:message></td>
                <td headers="t3" class="evenRowOddCol">
                    <%= bitstream.getFormatDescription() %>
<%    
    if (format.getSupportLevel() == 0)
    { %>
      <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\") +\"#unsupported\"%>">(<fmt:message key="jsp.submit.show-uploaded-file.notSupported"/>)</dspace:popup>
<%  }
    else if (format.getSupportLevel() == 1)
    { %>
      <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\") +\"#known\"%>">(<fmt:message key="jsp.submit.show-uploaded-file.known"/>)</dspace:popup>
<%  }
    else
    { %>
      <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\") +\"#supported\"%>">(<fmt:message key="jsp.submit.show-uploaded-file.supported"/>)</dspace:popup>
<%  } %>
                </td>
<%
    if (showChecksums)
    {
%>
                <td headers="t4" class="evenRowEvenCol">
                    <code><%= bitstream.getChecksum() %> (<%= bitstream.getChecksumAlgorithm() %>)</code>
                </td>
<%
    }
%>
            </tr>
        </table>

        <center>

            <p>
               <%--  <input type="submit" name="submit_format_<%= bitstream.getID() %>" value="Click here if this is the wrong format" /> --%>
			    <input type="submit" name="submit_format_<%= bitstream.getID() %>" value="<fmt:message key="jsp.submit.show-uploaded-file.click1.button"/>" />
            </p>
        </center>

        <center>
            <p>
                <%-- <input type="submit" name="submit_remove_<%= bitstream.getID() %>" value="Click here if this is the wrong file"> --%>
				<input type="submit" name="submit_remove_<%= bitstream.getID() %>" value="<fmt:message key="jsp.submit.show-uploaded-file.click2.button"/>" />
            </p>
        </center>

        <br/>

		<p class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info3"/></p>
        <ul class="uploadHelp">
			<li class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info4"/></li>
<%
    if (showChecksums)
    {
%>	
			<li class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info5"/>
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#checksum\"%>"><fmt:message key="jsp.submit.show-uploaded-file.info6"/></dspace:popup></li>
<%
    }
    else
    {
%>
  		<li class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info7"/>
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#checksum\"%>"><fmt:message key="jsp.submit.show-uploaded-file.info8"/></dspace:popup><input type="submit" name="submit_show_checksums" value="<fmt:message key="jsp.submit.show-uploaded-file.show.button"/>" /></li>
<%
    }
%>
        </ul>
        <br />

		<%-- Hidden fields needed for SubmissionController servlet to know which step is next--%>
        <%= SubmissionController.getSubmissionParameters(context, request) %>

<%-- HACK: Center used to align table; CSS and align="center" ignored by some browsers --%>
        <center>
            <table border="0" width="80%">
                <tr>
                    <td width="100%">&nbsp;</td>
				<%  //if not first step, show "Previous" button
					if(!SubmissionController.isFirstStep(request, subInfo))
					{ %>
                    <td>
                        <%-- <input type="submit" name="submit_prev" value="&lt; Previous"> --%>
						<input type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>" value="<fmt:message key="jsp.submit.general.previous"/>" />
                    </td>
				<%  } %>
                    <td>
                       <%--  <input type="submit" name="submit_next value="Next &gt;"> --%>
					    <input type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>" value="<fmt:message key="jsp.submit.general.next"/>" />
                    </td>
                    <td>&nbsp;&nbsp;&nbsp;</td>
                    <td align="right">
                        <%-- <input type="submit" name="submit_cancel" value="Cancel/Save"> --%>
						<input type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>" value="<fmt:message key="jsp.submit.general.cancel-or-save.button"/>" />
                    </td>
                </tr>
            </table>
        </center>
    </form>

</dspace:layout>
