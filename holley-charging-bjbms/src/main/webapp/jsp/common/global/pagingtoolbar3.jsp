<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<style>
.page-btn{
	padding: 0 0;
	margin-top:-5px;
	background: transparent;
	border: none;
	outline: none;
}

</style>
<div class="text-right" id="pagingToolbar3">
	<ul class="pagination">
  	  <li>
	  	<button id="firstPage3" class="btn btn-md page-btn" disabled="disabled">
	  		<span class="glyphicon glyphicon-step-backward" style="color:#337ab7;"></span>
  		</button>
  	  </li>
	  <li>
	  	<button id="prevPage3" class="btn btn-md page-btn" disabled="disabled">
	  		<span class="glyphicon glyphicon-triangle-left" style="color:#337ab7;"></span>
  		</button>
  	  </li>
	</ul>
	当前第  <input class="text-center" id="currentPage3" type="text" disabled="disabled" value='1' style="width: 80px;color: #337ab7;"/>  页,
	共  <span id="totalPage3" style="color:#337ab7;">0</span>  页,
	共  <span id="totalProperty3" style="color:#337ab7;">0</span>  条记录
	<ul class="pagination">
	  <li>
	  	<button id="nextPage3" class="btn btn-md page-btn">
	  		<span class="glyphicon glyphicon-triangle-right" style="color:#337ab7;"></span>
  		</button>
  	  </li>
  	  <li>
	  	<button id="lastPage3" class="btn btn-md page-btn" disabled="disabled">
	  		<span class="glyphicon glyphicon-step-forward" style="color:#337ab7;"></span>
  		</button>
  	  </li>
	</ul>
</div>
