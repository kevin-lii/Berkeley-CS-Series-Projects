<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang='en'>
  <head>
  	<title>MessageService</title>
  </head>

  <Body>
    <header>
    	<!-- Script -->
		 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
		 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
		 <script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.9.1/underscore-min.js"></script>
	     <script src="https://cdnjs.cloudflare.com/ajax/libs/backbone.js/1.3.3/backbone-min.js"></script>
	     <script src="<%=request.getContextPath() + "/public/js/stickit.js"%>"></script>
		 <script language="javascript1.2"type="text/javascript">
			$(function() {
		 	var Value = Backbone.Model.extend({
				defaults: function() {
					return {
						source: "",
						key: "",
						value: ""
					};
				},
		 		urlRoot: "http://localhost:8080/PropertyLocatorSpring/rest/resources/",
		 		url: function() {
		 			if (this.get('value') == null) {	
		 		      return this.urlRoot + this.get('source') + '/' + this.get('key');
		 	    	}
		 	    	return this.urlRoot + this.get('source') + '/' + this.get('key') + '/' + this.get('value');
		 		},
				sync: function(method, model, options) {
					var beforeSend = options.beforeSend;
					options.beforeSend = function(xhr) {
					      xhr.setRequestHeader("Authorization", "Bearer " + token);
					      if (beforeSend) {
					          return beforeSend.apply(this, arguments);
					      }
					};
					var success = options.success;
					options.success = function(response, responseText, xhr) {
					      jwt = xhr.getResponseHeader("x-jwt");
					      if (jwt) {token = jwt;}
					      if (success) {
					          success.apply(this, arguments);
					      }
					};
					Backbone.sync.call(this, method, model, options);
				},
			  });
			  var AppView = Backbone.View.extend({
			      bindings: {
					".source": "source",
					".key": "key",
					".value": "value",
					".modal-text": "value",
				  }, 
				  el: "#main",
				  events: {
					"click #submit": "submitEntry",
					"click #clear": "clear"
				},
				initialize: function() {
					this.listenTo(this.model, "sync", this.displayModal);
				},
				render: function() {
					this.stickit();	
					return this;
				},
				submitEntry: function() {
					var val = this.$('.value').val();
					if (val.length > 0) {
					    this.model.save();
					} else {
						this.model.fetch();
					}
				},
				displayModal: function() {
					$('#myModal').modal('show');
				},
				clear: function() {
					this.$('.source').val('');
					this.$('.key').val('');
					this.$('.value').val('');
				    this.model.unset('value');
				}
			  });
			  var appView = new AppView({model: new Value()});
			  appView.render();
			  token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM1NCIsImlhdCI6MTUzMjU1ODQ1MCwiZXhwIjoxNTMyNTU5OTUwfQ.lW51BFyzhTlNzHjOte2yRtJupSJiWtBOjbu73jJ9wKI";
			});
	  	</script>
   		<h1>Message Service</h1>
    </header>
    <section id="main">
	    <!--Buttons-->
	    <form id="form">
			<input class="source" type="text" placeholder="Properties File">
			<input class="key" type="text" placeholder="Key">
			<input class="value" type="text" placeholder="Value"> <br/>
		</form>
        <button class="btn btn-primary" type="button" id="submit">Submit</button>
        <button class="btn btn-secondary" type="button" id="clear">Clear</button>
	           
      	<!-- Modal -->
		<div class="modal fade" id="myModal">
			<div class="modal-dialog">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal">&times;</button>
		      		</div>
		      		<div class="modal-body">
		        		<p class="modal-text"></p>
		      		</div>
		      		<div class="modal-footer">
		        		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		      		</div>
		    	</div>
		  	</div>
		</div>
	</section>
  </Body>
</html>