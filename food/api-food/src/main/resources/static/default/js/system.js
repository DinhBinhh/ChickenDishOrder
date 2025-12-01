document.addEventListener('DOMContentLoaded', function() {
    // Initialize DataTables
    const initDataTables = () => {
        $('#usersTable, #rolesTable').DataTable({
            responsive: true,
            language: {
                search: "_INPUT_",
                searchPlaceholder: "Search...",
                lengthMenu: "Show _MENU_ entries"
            }
        });
    };

    // Handle edit user button
    const setupEditUserHandler = () => {
        $(document).on('click', '.edit-user', function() {
            const username = $(this).data('username');
            const enabled = $(this).data('enabled') == 1;

            // Get user's current roles
            const roles = [];
            $(this).closest('tr').find('.badge-role').each(function() {
                roles.push($(this).text());
            });

            // Fill the edit form
            const form = $('#editUserModal form');
            form.find('input[name="username"]').val(username);
            form.find('input[name="displayUsername"]').val(username);
            form.find('input[name="enabled"]').prop('checked', enabled);

            // Uncheck all roles first
            form.find('input[type="checkbox"][name="roles"]').prop('checked', false);

            // Check current roles
            roles.forEach(role => {
                form.find(`input[type="checkbox"][name="roles"][value="${role}"]`).prop('checked', true);
            });

            // Show modal
            new bootstrap.Modal(document.getElementById('editUserModal')).show();
        });
    };

    // Handle delete user button
    const setupDeleteUserHandler = () => {
        $(document).on('click', '.delete-user', function() {
            if (confirm('Are you sure you want to delete this user?')) {
                const username = $(this).data('username');
                window.location.href = `/system/users/delete?username=${encodeURIComponent(username)}`;
            }
        });
    };

    // Handle delete role button
    const setupDeleteRoleHandler = () => {
        $(document).on('click', '.delete-role', function() {
            const username = $(this).data('username');
            const role = $(this).data('role');

            if (confirm(`Are you sure you want to remove ${role} from ${username}?`)) {
                window.location.href = `/system/roles/delete?username=${encodeURIComponent(username)}&role=${encodeURIComponent(role)}`;
            }
        });
    };

    // Handle edit role button
    const setupEditRoleHandler = () => {
        $(document).on('click', '.edit-role', function() {
            const username = $(this).data('username');
            const role = $(this).data('role');

            $('#editRoleModal input[name="username"]').val(username);
            $('#editRoleModal input[name="currentRole"]').val(role);

            // Hiển thị modal
            new bootstrap.Modal(document.getElementById('editRoleModal')).show();
        });
    };

    // Initialize all handlers
    initDataTables();
    setupEditUserHandler();
    setupDeleteUserHandler();
    setupDeleteRoleHandler();
    setupEditRoleHandler();
});