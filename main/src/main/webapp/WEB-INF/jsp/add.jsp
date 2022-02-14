<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<c:set var="title" value="Add"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <form action="/addChannel" method="post" name="link">

        <div class="form-group">
        <label for="link">Link on Channel:</label>
            <input class="form-control" placeholder="Link in format @sad_sirko" id="link" name="link">
        </div>
        <button type="submit" class="btn btn-primary">Add channel</button>
    </form>

    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>
<script src="/js/form-validation.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
<!-- this should go after your </body> -->
<link rel="stylesheet" type="text/css"/>

</html>