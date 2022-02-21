<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<c:set var="title" value="Home"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div class="row">
        <div class="col-md-5">
                <h3 class="title"> ${category.name}</h3>
                <i>id </i>: ${category.id}
                <ul class="list-inline">
                    <li class="list-inline-item">
                                <form class="form-inline" action="/category" method="get">
                                        <input type="hidden" name="category"  value="${category}">
                                        <input type="hidden" name="categoryName" value="${category.name}">
                                    <button type="submit" class="btn btn-outline-primary"> Check </button>
                                </form>
                    </li>
                </ul>
                <hr>

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
