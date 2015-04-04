<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="/js/jquery-2.1.1.min.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="/css/niceforms-default.css" />
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
<link rel="stylesheet" type="text/css" href="/css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="/css/demo.css" />
		<link rel="stylesheet" type="text/css" href="/css/component.css" />
</head>
<body>
	<admin:menu />
	<form:form commandName="speaker" method="post"
		action="/admin/speaker/add" enctype="multipart/form-data">
		<div class="info_space">
		<fieldset>
			<legend>Add/Edit Speaker</legend>
			<p>
				<form:errors />
			</p>
			<dl>
				<dt>
					<label for="firstName">First Name</label>
				</dt>
				<dd>
					<form:input path="firstName" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="lastName">Last Name</label>
				</dt>
				<dd>
					<form:input path="lastName" />
				</dd>
			</dl>
				<dl>
				<dt>
					<label for="email">Email</label>
				</dt>
				<dd>
					<form:input path="email" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="phone">Phone</label>
				</dt>
				<dd>
					<form:input path="phone" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="twitter">Twitter</label>
				</dt>
				<dd>
					<form:input path="twitter" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="bio">Bio</label>
				</dt>
				<dd>
					<form:textarea path="bio" cols="50" rows="5" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="headline">Headline</label>
				</dt>
				<dd>
					<form:textarea path="headline" cols="50" rows="5" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="file">Picture ( MUST be 280x326)</label>
				</dt>
				<dd>
					<input name="file" type="file" id="imgInp"/>
				</dd>
			</dl>
			<sec:csrfInput />
			<form:hidden path="id" />
			<button type="submit">Save</button>
		</fieldset>
		</div>
		<div class="upload_space">
		<fieldset name ="upload_speaker_image">
			<legend>Upload Speaker Image</legend>
			<div class="container">
			<!-- Top Navigation -->
			<div class="content">
				<div class="component">
					<div class="overlay">
						<div class="overlay-inner">
						</div>
					</div>
					<!-- This image must be on the same domain as the demo or it will not work on a local file system -->
					<!-- http://en.wikipedia.org/wiki/Cross-origin_resource_sharing -->
					<img id="uploading-image" class="resize-image" alt="image for resizing">
					<button class="btn-crop js-crop">Crop<img class="icon-crop"></button>
				</div>
			</div><!-- /content -->
		</div> <!-- /container -->
		</fieldset>
	</form:form>
	</div>
	<script language="javascript" src="/js/component.js"></script>
	<script language="javascript" type="text/javascript" src="/js/script.js"></script>
	<script  type="text/javascript" src="/js/niceforms.js"></script>
	<script src="/js/preview_image.js"></script>
	</body>
</html>