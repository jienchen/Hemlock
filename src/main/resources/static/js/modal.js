$(document).ready(function(){
	$('.photo').on('click', function () {
		var image = $(this).attr('src');
		$('.card .modal').on('show.bs.modal', function () {
			$(".card .modalImg").attr('src', image);
		});	
		$('.card .modal').modal();
	});
});