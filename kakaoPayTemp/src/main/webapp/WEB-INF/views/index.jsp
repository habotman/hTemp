<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge;chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Example</title>
<link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon" />

<script src="/websrc/lib/jquery-3.4.1.min.js"></script>

<style>
table.info {
	border: 0px solid #444444;
	border-collapse: collapse;
	font-size: 8pt;
	width: 100%;
	min-width: 720px;
	background-color: #6f6e6e12;
}

.info th, .info td {
	font-size: 8pt;
	border: 0px solid #444444;
	padding: 2px;
}

table.list {
	font-size: 8pt;
	border: 1px solid #444444;
	border-collapse: collapse;
	width: 100%;
	min-width: 720px;
	margin-top: 36px;
	background-color: #6f6e6e12;
	font-size: 8pt;
}

.list th, .list td {
	border: 1px solid #444444;
	padding: 2px;
}

.list td {
	background-color: #fff;
}
</style>
</head>

<!-- c4d0c478820dfa8980d72bad6715458d -->

<body>

    <table class='info' style="" id='tb_card_info'>
        <colgroup>
            <col width="95">
            <col width="180">
            <col width="95">
            <col width="*">
        </colgroup>
        <thead>
            <tr>
                <td colspan="4" style="padding-left: 15px;">
                    <h3>결제요청 정보</h3>
                </td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <th>거래금액</th>
                <td colspan="3">
                    <input type="text" id="payment" name="payment" onKeyDown="javascript:onlyNumber(event)" maxlength="10" autocomplete="off" placeholder="거래금액을 입력하세요" style="IME-MODE: disabled; width: 146px;" numberOnly />
                </td>
            </tr>
            <tr>
                <th>카드번호</th>
                <td colspan="3">
                    <input id="card1" type="text" min="4" maxlength="4" style="width: 46px; IME-MODE: disabled; width: 146px;" numberOnly onKeyDown="javascript:onlyNumber(event)">
                    -
                    <input id="card2" type="text" min="4" maxlength="4" style="width: 46px; IME-MODE: disabled; width: 146px;" numberOnly onKeyDown="javascript:onlyNumber(event)">
                    -
                    <input id="card3" type="text" min="4" maxlength="4" style="width: 46px; IME-MODE: disabled; width: 146px;" numberOnly onKeyDown="javascript:onlyNumber(event)">
                    -
                    <input id="card4" type="text" min="4" maxlength="4" style="width: 46px; IME-MODE: disabled; width: 146px;" numberOnly onKeyDown="javascript:onlyNumber(event)">
                </td>
            </tr>
            <tr>
                <th>할부개월</th>
                <td colspan="3">
                    <select id="installment" tyle="width: 46px;">
                        <option value="0" selected="selected">일시불</option>
                        <option value="2">2개월</option>
                        <option value="3">3개월</option>
                        <option value="4">4개월</option>
                        <option value="5">5개월</option>
                        <option value="6">6개월</option>
                        <option value="7">7개월</option>
                        <option value="8">8개월</option>
                        <option value="9">9개월</option>
                        <option value="10">10개월</option>
                        <option value="11">11개월</option>
                        <option value="12">12개월</option>
                    </select>
                </td>
            </tr>
            <tr style="margin-bottom: 10px;">
                <th>유효일자</th>
                <td>                     
                      <select id="available_month" tyle="width: 46px;">
                        <option value="1" selected="selected">1월</option>
                        <option value="2">2월</option>
                        <option value="3">3월</option>
                        <option value="4">4월</option>
                        <option value="5">5월</option>
                        <option value="6">6월</option>
                        <option value="7">7월</option>
                        <option value="8">8월</option>
                        <option value="9">9월</option>
                        <option value="10">10월</option>
                        <option value="11">11월</option>
                        <option value="12">12월</option>
                    </select>
                     
                    
                     <select id="available_year" tyle="width: 46px;">
                        <option value="20" selected="selected">2020</option>
                        <option value="21">2021</option>
                        <option value="22">2022</option>
                        <option value="23">2023</option>
                        <option value="24">2024</option>
                        <option value="25">2025</option>
                        <option value="26">2026</option>
                        <option value="27">2027</option>
                        <option value="28">2028</option>
                        <option value="29">2029</option>
                        <option value="30">2030</option>
                        <option value="31">2031</option>
                    </select>
                    
                </td>
                <th>CVC</th>
                <td>
                    <input id="cvc" type="text" min="2" maxlength="3" style="width: 75%;">
                </td>
            </tr>
        </tbody>

        <tfoot style="">
            <tr style="border-top: 1px solid; margin-top: 10px;">
                <td colspan="4" style="text-align: right; padding: 10px 15px 10px 10px;">
                    <button type="button" class="" onclick="javascript:f_PaymentCard();">&nbsp;카드결제&nbsp;</button>
                </td>
            </tr>
        </tfoot>
    </table>


    <table class="list" style="">
        <colgroup>
            <%-- 
			<col width="95">
			<col width="120">
			<col width="95">
			<col width="*"> --%>
        </colgroup>
        <thead>
            <tr>
                <th>결제처리ID</th>
                <th>결제시각</th>
                <th>관리번호</th>
                <th>거래상태</th>
                <th>거래금액</th>
                <th>부가가치세</th>
                <th>카드번호</th>
                <th>할부개월</th>
                <th>유효일자</th>
                <th>CVC</th>
                <th>원거래관리번호</th>
                <th>암호화 카드정보</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>

            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
        </tbody>
    </table>

</body>



<script type="text/javascript">
	$(document).ready(function() {

	});

	/*
	 * Func. 카드결제
	 */
	function f_PaymentCard() {
		var payTarget = $('#tb_card_info');
		var param = {
			payment : payTarget.find('#payment').val(),
			card1 : payTarget.find('#card1').val(),
			card2 : payTarget.find('#card2').val(),
			card3 : payTarget.find('#card3').val(),
			card4 : payTarget.find('#card4').val(),
			install_month : payTarget.find('#installment').val(),
			ava_month : payTarget.find('#available_month').val(),
			ava_year : payTarget.find('#available_year').val(),
			cvc : payTarget.find('#cvc').val()
		};

		console.log("카드결제 요청 Parameters:>>" + JSON.stringify(param, null, 4));
		callAjax("reqSettlement", param, function() {
			alert("성공???");
		});

	}
	/**
	 * Ajax 공통함수
	 */
	var callAjax = function(command, parmData, callback) {
		var d = $.Deferred();

		$.ajax({
			url : "" + command,
			method : 'POST',
			dataType : 'json'
			// , jsonp   : "callback"
			,
			timeout : "10000",
			data : parmData
		}).done(function(response) {

			console.log(JSON.stringify(response, null, 4));
			d.resolve(response);
			callback(response);
		});

		return d.promise();
	}

	var loadProperty = function(parmData, callback) {
		var d = $.Deferred();

		$.ajax({
			url : "/SVC90/prop",
			method : 'POST',
			dataType : 'json'
			// , jsonp   : "callback"
			,
			timeout : "10000",
			data : parmData
		}).done(function(response) {

			d.resolve(response.PROP);
			callback(response.PROP);
			console.log(JSON.stringify(response.PROP, null, 4));
		});

		return d.promise();
	}

	/*숫자형 Input 이벤트 처리 	*/
	function onlyNumber(evt) {
		if (window.event) { // IE코드
			var code = window.event.keyCode;
		} else { // 타브라우저
			var code = evt.which;
		}
		if ((code > 34 && code < 41) || (code > 47 && code < 58)
				|| (code > 95 && code < 106) || code == 8 || code == 9
				|| code == 13 || code == 46) {
			window.event.returnValue = true;
			return;
		}
		if (window.event) {
			window.event.returnValue = false;
		} else {
			evt.preventDefault();
		}
	}
</script>
</html>