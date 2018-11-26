<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

%>
<html lang="en">

  <head>
  	<title>MessageService</title>
  </head>

  <Body>
    <header>
    	<!-- Script -->
		 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
		 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

   		<h1>Message Service</h1>
    </header>
    <section id="main">
	    <!--Buttons-->
	    <form method="get" name="sourceKeyForm" action="StrutsLocator.do">
		<input class="source" name= "source" type="text" placeholder="Properties File">
		<input class="key" name = "key" type="text" placeholder="Key"> <br/>
	    <button id="submit">Submit</button>
	    </form>

    </section>
  </Body>

</html>