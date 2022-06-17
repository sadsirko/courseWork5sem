<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" session="false" %>

<%--@elvariable id="url" type="java.lang.String"--%>
<%--@elvariable id="total_records" type="java.lang.Integer"--%>
<%--@elvariable id="current_page" type="java.lang.Integer"--%>
<%--@elvariable id="page_size" type="java.lang.Integer"--%>

<style type="text/css">
    div.pagination {
        margin-top: 10px;
        text-align: center;
    }
    .pagination a {
        color: #3b5998;
        text-decoration: none;
        border: 1px #c2d1df solid;
        padding: 2px 5px;
        margin-right: 5px;
    }
    .pagination a:hover {
        color: #3b5998;
        text-decoration: none;
        border: 1px #3b5998 solid;
    }
    a.current {
        background: #c2d6ed;
    }
</style>

<div class="pagination">
    <a href="${url}&page=${current_page-1>=0?current_page-1:0}" class="prev">&lt; Prev</a>
    <%
        Integer total_records = (Integer) request.getAttribute("total_records");
        Integer page_size = (Integer) request.getAttribute("page_size");
        Integer current_page = (Integer) request.getAttribute( "current_page" );
        int pages = total_records / page_size;
        int lastPage = pages * page_size < total_records ? pages : pages - 1;
        request.setAttribute("last_page", lastPage);
        // сколько ссылок отображается начиная с самой первой (не может быть установлено в 0)
        final int N_PAGES_FIRST = 1;
        // сколько ссылок отображается слева от текущей (может быть установлено в 0)
        final int N_PAGES_PREV = 1;
        // сколько ссылок отображается справа от текущей (может быть установлено в 0)
        final int N_PAGES_NEXT = 1;
        // сколько ссылок отображается в конце списка страниц (не может быть установлено в 0)
        final int N_PAGES_LAST = 1;
        if (N_PAGES_FIRST < 1 || N_PAGES_LAST < 1) throw new AssertionError(  );
        // показывать ли полностью все ссылки на страницы слева от текущей, или вставить многоточие
        boolean showAllPrev;
        // показывать ли полностью все ссылки на страницы справа от текущей, или вставить многоточие
        boolean showAllNext;
        showAllPrev = N_PAGES_FIRST >= (current_page - N_PAGES_PREV);
        showAllNext = current_page + N_PAGES_NEXT >= lastPage - N_PAGES_LAST;
        request.setAttribute( "N_PAGES_FIRST", N_PAGES_FIRST );
        request.setAttribute( "N_PAGES_PREV", N_PAGES_PREV );
        request.setAttribute( "N_PAGES_NEXT", N_PAGES_NEXT );
        request.setAttribute( "N_PAGES_LAST", N_PAGES_LAST );
        request.setAttribute( "showAllPrev", showAllPrev );
        request.setAttribute( "showAllNext", showAllNext );
    %>
    <%-- show left pages --%>
    <c:choose>
        <c:when test="${showAllPrev}">
            <c:if test="${current_page > 0}">
                <c:forEach begin="0" end="${current_page - 1}" var="p">
                    <a href="${url}&page=${p}">${p + 1}</a>
                </c:forEach>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:forEach begin="0" end="${N_PAGES_FIRST - 1}" var="p">
                <a href="${url}&page=${p}">${p + 1}</a>
            </c:forEach>
            <span style="margin-right: 5px">...</span>
            <c:forEach begin="${current_page - N_PAGES_PREV}" end="${current_page - 1}" var="p">
                <a href="${url}&page=${p}">${p + 1}</a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <%-- show current page --%>
    <a href="${url}&page=${current_page}" class="current">${current_page + 1}</a>
    <%-- show right pages --%>
    <c:choose>
        <c:when test="${showAllNext}">
            <c:forEach begin="${current_page + 1}" end="${last_page}" var="p">
                <a href="${url}&page=${p}">${p + 1}</a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach begin="${current_page + 1}" end="${current_page + 1 + (N_PAGES_NEXT - 1)}" var="p">
                <a href="${url}&page=${p}">${p + 1}</a>
            </c:forEach>
            <span style="margin-right: 5px">...</span>
            <c:forEach begin="${last_page - (N_PAGES_LAST - 1)}" end="${last_page}" var="p">
                <a href="${url}&page=${p}">${p + 1}</a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <a href="${url}&page=${current_page + 1 > last_page ? last_page : current_page + 1}" class="next">Next &gt;</a>
</div>