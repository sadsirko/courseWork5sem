<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<c:set var="title" value="Home"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div class="row">
    categories
    </div>
    <div class="row">
        <div class="col-md-5">
             <c:forEach var="source" items="${nameList}">
                <h3 class="title"> ${source.name}</h3>
                <i>id </i>: ${source.id}
                
                <ul class="list-inline">
                    <li class="list-inline-item">
                                <form class="form-inline" action="/category/check" method="post">
                                        <input type="hidden" name="source"  value="${source}">
                                        <input type="hidden" name="sourceId" value="${source.id}">
                                        <input type="hidden" name="sourceName" value="${source.name}">
                                    <button type="submit" class="btn btn-outline-primary"> Check </button>
                                </form>
                    </li>
                </ul>
                <hr>
            </c:forEach>
        </div>
    
        <div class="col-md-5">
           <c:if test="${not empty chooseList }">
            <c:forEach var="choose" items="${chooseList}">
                <h3 class="title"> ${choose.name}</h3>
                <i>id </i>: ${choose.id}
                
                <ul class="list-inline">
                    <li class="list-inline-item">
                                <form class="form-inline" action="/home/remove" method="get">
                                        <input type="hidden" name="category" value="${choose}">
                                        <input type="hidden" name="categoryId" value="${choose.id}">
                                        <input type="hidden" name="categoryName" value="${choose.name}">
                                    <button type="submit" class="btn btn-outline-primary"> Remove </button>
                                </form>
                    </li>
                </ul>
                <hr>
            </c:forEach>
           </c:if>
        </div>
</div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>

</body>
</html>
