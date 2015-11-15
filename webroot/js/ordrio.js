$(function () {
	if(window.location.pathname === '/') {
		renderCreateOrdrView($);
	}
	else if(stringStartsWith(window.location.pathname, '/o/')) {
		renderOrdrView($);
	}
});

function renderCreateOrdrView($) {
	$('#create-ordr').removeClass('hidden');
	$('#create-ordr-form').submit(function(e) {
		e.preventDefault();
		var formData = $(e.target).serialize();
		$.post('/api/ordr', formData)
				.success(function(data) {
					window.location.href = '/o/'+JSON.parse(data).id;
				});
	});
}

function renderOrdrView($) {
	$('#ordr').removeClass('hidden');

	var wsPort = window.location.port === '' ? '' : ':' + window.location.port;
	var wsUrl = window.location.protocol + '//' + window.location.hostname + wsPort;
	var sock = new SockJS(wsUrl+'/socket');

	sock.onopen = function() {
		console.log('open');
	};

	sock.onmessage = function(e) {
		var aggregate = JSON.parse(e.data);
	};

	sock.onclose = function() {
		console.log('close');
	};
}

function stringStartsWith (string, prefix) {
	return string.slice(0, prefix.length) == prefix;
}