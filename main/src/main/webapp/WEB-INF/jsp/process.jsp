<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<c:set var="title" value="Process"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <form action="/processing" method="post" name="process">
        <div class="row form-group">
            <div class="col">
                <label for="startDate">Start date:</label>
                <input type="text" class="form-control datetimepicker" id="startDate"
                       placeholder="Choose start day"
                       name="startDate" autocomplete="off">
            </div>
            <div class="col">
                <label for="endDate">End date:</label>
                <input type="text" class="form-control datetimepicker" id="endDate"
                       placeholder="Choose end date"
                       name="endDate" autocomplete="off">
            </div>
        </div>

        <div class="form-group">
            <label for="stop">Stop words:</label>
            <input class="form-control" placeholder="Enter additional stop words separated with space" id="stop" name="stop"
             height: 50px autocomplete="off">
        </div>

        <div class="form-group">
            <label for="symbolNum">Number of Symbols:</label>
            <input type="number" class="form-control" id="symbolNum" name="symbolNum"
                   placeholder="Enter num of symbols" min="50" max="1000000"
                   autocomplete="off">
        </div>

        <div class="form-group">
                    <label for="range">Range:</label>
                    <input type="number" class="form-control" id="range" name="range"
                           placeholder="Enter num of symbols" min="0" max="1000000"
                           autocomplete="off">
        </div>
        <button type="submit" class="btn btn-primary">Process</button>
    </form>


    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>


<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/jquery.validate.min.js"></script>
<script src="/js/form-validation.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
<!-- this should go after your </body> -->
<link rel="stylesheet" type="text/css" href="/css/jquery.datetimepicker.css"/>
<script src="/js/jquery.js"></script>
<script src="/js/jquery.datetimepicker.full.min.js"></script>
<script>
    jQuery.noConflict();
    jQuery('.datetimepicker').datetimepicker({
        format: 'd/m/y H:i'
    });
</script>
</html>