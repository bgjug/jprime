<!DOCTYPE html>
<html lang="en" class="no-js">
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
		<meta name="viewport" content="width=device-width, initial-scale=1"> 
		<title>Image Resizing with Canvas</title>
		<meta name="description" content="Learn how to resize images using JavaScript and the HTML5 Canvas element using controls, commonly seen in photo editing applications." />
		<meta name="keywords" content="canvas, javascript, HTML5, resizing, images" />
		<meta name="author" content="Codrops" />
		<link rel="stylesheet" type="text/css" href="css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="css/demo.css" />
		<link rel="stylesheet" type="text/css" href="css/component.css" />
		<!--[if IE]>
  		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
	</head>
	<body>
		<div class="container">
			<!-- Top Navigation -->
			<div class="content">
				<header class="codrops-header">
					<h1>Uploading Speaker Image</h1>
				</header>
				<div class="component">
					<div class="overlay">
						<div class="overlay-inner">
						</div>
					</div>
					<!-- This image must be on the same domain as the demo or it will not work on a local file system -->
					<!-- http://en.wikipedia.org/wiki/Cross-origin_resource_sharing -->
					<input type="file" id="inputFile"/>
					<img id="uploading-image" class="resize-image" src="img/image.jpg" alt="image for resizing">
					<button class="btn-crop js-crop">Crop<img class="icon-crop" src="img/crop.svg"></button>
				</div>
			</div><!-- /content -->
		</div> <!-- /container -->
		<script src="js/jquery-2.1.1.min.js"></script>
		<script src="js/component.js"></script>
		<!---Link to redirectction script -->
		<!--<script type="text/javascript" src="/admin/js/script_redirect.js"></script>-->
	</body>
</html>