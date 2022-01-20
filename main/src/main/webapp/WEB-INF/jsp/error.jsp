<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<c:set var="title" value="Error"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>

<div class="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <c:choose>
        <c:when test="${not empty errorMessage}">
            <h2>${errorMessage}</h2>
        </c:when>
        <c:otherwise>
            <h1>Wrong URL</h1>
        </c:otherwise>
    </c:choose>
</div>
</div>

</body>
</html>
