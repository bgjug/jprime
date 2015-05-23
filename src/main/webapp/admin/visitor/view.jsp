<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<script type="text/javascript" src="/js/niceforms.js"></script>
	<script type="text/javascript" src="/js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
	<link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
	<link rel="stylesheet" type="text/css" href="/css/jquery.dataTables.css">
	<title>Insert title here</title>
</head>
<body>
	<admin:menu/>

	<fieldset>
	<legend>Visitors</legend>

		<div>
			<a href="/admin/visitor/add">Add</a>
			<a href="/admin/visitor/export">Export</a>
			<a href="/admin/visitor/send">Send email to ALL </a>
		</div>
		&nbsp;
		<br/>
		<br/>
		<b>Statistics:</b>&nbsp;Requesting: ${requestingCount}&nbsp;&nbsp;Payed:&nbsp;${payedCount}&nbsp;&nbsp;Sponsored:&nbsp;${sponsoredCount}
		<br/>
		<br/>
		<br/>
		<br/>
		<b>Current listed visitors: <span id="count"></span></b>
		<br/>
		<br/>

		<table id="visitors" class="display">
			<thead>
			<tr>
				<th><i>Name</i></th>
				<th><i>Email</i></th>
                <th><i>Company</i></th>
				<th><i>Payed</i></th>
				<th><i>Registrant</i></th>
				<th><i>Invoice number</i></th>
				<th><i>Operations</i></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="visitor" items="${visitors}">
				<tr>
					<td>${visitor.name}</td>
					<td>${visitor.email}</td>
                    <td>${visitor.company}</td>
					<td>${visitor.status}</td>
					<td>${visitor.registrant.name}</td>
					<td>${not empty visitor.registrant and not empty visitor.registrant.realInvoiceNumber ?visitor.registrant.realInvoiceNumber:''}</td>
					<td>

						<span><a href="/admin/visitor/edit/${visitor.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;

						<span><a href="/admin/visitor/remove/${visitor.id}"> Remove </a></span>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<script>
		$(document).ready(function() {
			initTable();
			countVisitors();
		} );

		var initTable=function(){
			$('#visitors').DataTable( {
				"aaSorting": [],
				initComplete: function () {
					this.api().columns().every( function () {
						var column = this;
						if ($(column.header()).text() !== 'Operations') {
							var select = $('<br/><select><option value=""></option></select>')
									.appendTo($(column.header()))
									.on('change', function () {
										var val = $.fn.dataTable.util.escapeRegex(
												$(this).val()
										);

										column
												.search(val ? '^' + val + '$' : '', true, false)
												.draw();
										countVisitors();
									});

							column.data().unique().sort().each(function (d, j) {
										select.append('<option value="' + d + '">' + d + '</option>')
									}
							);

						}
					} );
				}
			} );

			var dtable = $("#visitors").dataTable().api();
			$(".dataTables_filter input")
					.unbind()
					.bind("input", function(e) {
						if(this.value.length >= 3 || e.keyCode == 13) {
							// Call the API search function
							dtable.search(this.value).draw();
							countVisitors();
						}
						// Ensure we clear the search if they backspace far enough
						if(this.value == "") {
							dtable.search("").draw();
							countVisitors();
						}
						return;
					});

		}

		var countVisitors = function(){
			// Initialize your table
			var oTable = $('#visitors').dataTable();

			// Get the length
			$('#count').text(oTable._('tr', {"filter": "applied"}).length);
		}
	</script>
</body>
</html>