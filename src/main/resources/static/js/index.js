$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

    // get the value of title and content
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();

	$.post(
	    "/community/discuss/add",
	    {"title": title, "content": content},
	    function(data) {
	        data = $.parseJSON(data);
            $("#hintModalLabel").text(data.msg);
            $("#hintModal").modal("show");
            	setTimeout(function(){
            		$("#hintModal").modal("hide");

            		// refresh if post success
            		if(data.code == 0) {
            		    window.location.reload();
            		}
            	}, 2000);
	    }
	)

}