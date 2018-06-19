$(document).ready(function(){
	$('#edit').on('click', function () {
		var image = $(this).attr('src');
		$('#picModal').on('show.bs.modal', function () {
		});
		$('#picModal').modal();
	});
});