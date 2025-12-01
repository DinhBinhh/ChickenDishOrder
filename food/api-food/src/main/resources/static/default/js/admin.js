document.addEventListener('DOMContentLoaded', function() {
    // Initialize DataTables with Vietnamese language
    $('#productsTable').DataTable({
        language: {
            "sProcessing": "Đang xử lý...",
            "sLengthMenu": "Hiển thị _MENU_ mục",
            "sZeroRecords": "Không tìm thấy kết quả",
            "sInfo": "Hiển thị _START_ đến _END_ của _TOTAL_ mục",
            "sInfoEmpty": "Hiển thị 0 đến 0 của 0 mục",
            "sInfoFiltered": "(lọc từ _MAX_ mục)",
            "sSearch": "Tìm kiếm:",
            "oPaginate": {
                "sFirst": "Đầu",
                "sPrevious": "Trước",
                "sNext": "Tiếp",
                "sLast": "Cuối"
            }
        }
    });

    // Handle image preview for both add and edit modals
    $('input[type="file"]').change(function(e) {
        const input = e.target;
        const modal = $(input).closest('.modal');
        const imgPreview = modal.find('img');

        if (input.files && input.files[0]) {
            const reader = new FileReader();

            reader.onload = function(e) {
                imgPreview.attr('src', e.target.result).show();
            };

            reader.readAsDataURL(input.files[0]);
        }
    });

    // Auto-calculate formatted price display
    $('input[name="price"]').on('blur', function() {
        const price = $(this).val();
        if (price) {
            const formatted = new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(price);
            $(this).val(price); // Keep original value for submission
            $(this).next('.form-text').remove();
            $(this).after(`<small class="form-text text-muted">${formatted}</small>`);
        }
    });

    // Handle spicy level display
    $('input[name="spicyLevel"]').on('input', function() {
        const level = $(this).val();
        const preview = $(this).next('.spicy-preview');

        if (!preview.length) {
            $(this).after('<div class="spicy-preview mt-2"></div>');
        }

        let stars = '';
        for (let i = 0; i < 5; i++) {
            stars += i < level ? '🌶️' : '○';
        }
        $(this).next('.spicy-preview').text(`Độ cay: ${stars} (${level}/5)`);
    });

    // Initialize spicy level preview
    $('input[name="spicyLevel"]').trigger('input');

    // ==============================================
    // AUTO RELOAD PAGE WHEN SUCCESS MESSAGE APPEARS
    // ==============================================

    // Check for success message
    const successAlert = document.querySelector('.alert-success');
    if (successAlert) {
        // Hide the modal if it's open
        const addProductModal = bootstrap.Modal.getInstance(document.getElementById('addProductModal'));
        if (addProductModal) {
            addProductModal.hide();
        }

        // Reload page after 1.5 seconds
        setTimeout(() => {
            window.location.reload();
        }, 1500);
    }

    // Alternatively, check for success message in Thymeleaf variable
    const successMessage = /*[[${successMessage}]]*/ null;
    if (successMessage) {
        setTimeout(() => {
            window.location.href = '/admin'; // Clean reload
        }, 1500);
    }
    // Xử lý khi click nút Edit Product
    $(document).on('click', '.edit-product', function() {
        const productId = $(this).data('id');

        // Gọi API để lấy thông tin sản phẩm
        $.get(`/admin/products/edit/${productId}`, function(data) {
            // Điền dữ liệu vào form edit
            $('#editProductModal input[name="id"]').val(data.id);
            $('#editProductModal input[name="name"]').val(data.name);
            $('#editProductModal textarea[name="description"]').val(data.description);
            $('#editProductModal input[name="price"]').val(data.price);

            // Cập nhật ảnh hiện tại
            if(data.image) {
                $('#editProductModal .img-thumbnail').attr('src', `/default/images/${data.image}`);
                $('#editProductModal .img-thumbnail').show();
            } else {
                $('#editProductModal .img-thumbnail').hide();
            }

            // Hiển thị modal
            $('#editProductModal').modal('show');
        }).fail(function() {
            alert('Error loading product data');
        });
    });

    // Xử lý khi click nút Edit Customer
    $(document).on('click', '.edit-customer', function() {
        const customerId = $(this).data('id');

        // Gọi API để lấy thông tin khách hàng
        $.get(`/admin/customers/edit/${customerId}`, function(data) {
            // Điền dữ liệu vào form edit
            $('#editCustomerModal input[name="id"]').val(data.id);
            $('#editCustomerModal input[name="name"]').val(data.name);
            $('#editCustomerModal input[name="email"]').val(data.email);
            $('#editCustomerModal input[name="phone"]').val(data.phone);
            $('#editCustomerModal textarea[name="address"]').val(data.address);

            // Hiển thị modal
            $('#editCustomerModal').modal('show');
        }).fail(function() {
            alert('Error loading customer data');
        });
});