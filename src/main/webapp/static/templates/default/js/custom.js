var updateReviewsTimeoutId = 0;

var successHtml = '<div class="alert alert-success alert-dismissable">Success! \
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>';
var errorHtml = '<div class="alert alert-danger alert-dismissable">Failure! \
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>';

$(document).ready(function () {
    updateReviews(0);

    prepareCreateReviewButton();
    prepareReviewReasonModalForm();
});

function prepareReviewReasonModalForm() {
    $('#modal-reason-form').modal({show: false});
}

function updateReviews(delay) {
    clearTimeout(updateReviewsTimeoutId);

    updateReviewsTimeoutId = setTimeout(function () {
        updateOutgoingReview();
        updateIncomingReviews();
    }, delay);
}

function updateOutgoingReview() {
    $.ajax({
        url: '/review?type=outgoing',
        success: function (data) {
            $("div#review-outgoing").html(data['content']);
            prepareReviewChangeStatusButtons();

            updateReviews(1000);
        },
        error: function () {
            updateReviews(1000);
        }
    });
}

function updateIncomingReviews() {
    $.ajax({
        url: '/review?type=incoming',
        success: function (data) {
            $("div#reviews-incoming").html(data['content']);

            prepareReviewChangeStatusButtons();
        }
    });
}

function prepareCreateReviewButton() {
    $('.review-create').off().on('click', function () {
        var projectCode = $('.form-control.projects').find("option:selected").attr('value');

        $.ajax({
            url: '/review',
            type: 'POST',
            data: {"projectCode": projectCode},
            success: function (data) {
                if (data['error'] == false) {
                    $("div#notifications").html(successHtml);
                } else {
                    $("div#notifications").html(errorHtml);
                }
            },
            error: function () {
                $("div#notifications").html(errorHtml);
            }
        });
    });
}

function prepareReviewChangeStatusButtons() {
    $(".btn.btn-default.review-change-status").each(function () {
        $(this).off().on('click', function () {
            var reviewId = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            var reason = $(this).attr("data-reason");

            changeReviewStatus(reviewId, status, reason, "");
        });
    });

    $(".btn.btn-default.review-change-status-reason").off().on('click', function () {
        var reviewId = $(this).attr("data-id");
        var status = $(this).attr("data-status");

        var reviewChangeStatusModalButton = $('.btn.btn-default.review-change-status-modal');
        reviewChangeStatusModalButton.attr('data-id', reviewId);
        reviewChangeStatusModalButton.attr('data-status', status);

        $('#modal-reason-form').modal('show');
    });

    $(".btn.btn-default.review-change-status-modal").off().on('click', function () {
        var reviewId = $(this).attr("data-id");
        var status = $(this).attr("data-status");
        var reason = $('.review-reasons').find("option:selected").attr('value');
        var refuseDesc = $('.refuse-desc').val();

        changeReviewStatus(reviewId, status, reason, refuseDesc);

        $('#modal-reason-form').modal('hide');
    });
}

function changeReviewStatus(reviewId, status, reason, refuseDesc) {
    reason = reason !== 'undefined' ? reason : null;
    refuseDesc = refuseDesc !== 'undefined' ? refuseDesc : null;

    $.ajax({
        url: '/review/' + reviewId,
        type: 'POST',
        data: {"status": status, "reasonCode": reason, "refuseDesc": refuseDesc},
        success: function (data) {
            if (data['error'] == false) {
                $("div#notifications").html(successHtml);
            } else {
                $("div#notifications").html(errorHtml);
            }
        },
        error: function () {
            $("div#notifications").html(errorHtml);
        }
    });
}

function showNotification(notification) {
    $("div#notifications").html(notification);
}