$(document).ready(function() {
	$("#search-form").submit(function(event) {

		// stop submit the form, we will post it manually.
		event.preventDefault();

		fire_ajax_submit();

	});

});

$('#logo').on('click', function(event) {
	  event.preventDefault(); // To prevent following the link (optional)
		$('#logTable').hide();
		$('#error').hide();
		$('#searchString').val('');		
		$('#date').val('');
	});

function fire_ajax_submit() {
	var search = {}
	search["searchString"] = $("#searchString").val();
	search["date"] = $("#date").val();

	$("#btn-search").prop("disabled", true);

	$
			.ajax({
				type : "POST",
				contentType : "application/json",
				url : "/api/search",
				data : JSON.stringify(search),
				dataType : 'json',
				cache : false,
				timeout : 600000,
				success : function(data) {

					/*
					 * var json = "<h4>Response</h4><pre><p class=text-wrap>" +
					 * JSON.stringify(data, null, 4) + "</p></pre>";
					 * $('#feedback').html(json); var mydata =
					 * JSON.stringify(data, null, 4);
					 */
					var mydata = [
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: qwert ",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: asdfg",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							},
							{
								"searchString" : "org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through method 'setUserService' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService': Invocation of init method failed; nested exception is java.lang.Error: Unresolved compilation problems: zxcvb",
								"email" : "nithin@yahoo.com",
								"date" : "2017-03-09"
							} ];

					$('#logTable').show();
					$('#error').hide();
					$('#clienti').bootstrapTable({
						data : mydata
					});

					console.log("SUCCESS : ", data);
					$("#btn-search").prop("disabled", false);

				},
				error : function(e) {
					$('#logTable').hide();
					$('#error').show();
					var json = "<h4>Response</h4><pre>" + e.responseText
							+ "</pre>";
					$('#error').html(json);

					console.log("ERROR : ", e);
					$("#btn-search").prop("disabled", false);

				}
			});

}