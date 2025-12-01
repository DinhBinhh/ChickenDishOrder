// main.js
document.addEventListener('DOMContentLoaded', function() {
    // Header scroll effect - Bản nâng cấp
    const header = document.querySelector('.main-header');
    let lastScroll = 0;
    const scrollThreshold = 100; // Ngưỡng cuộn để ẩn header

    window.addEventListener('scroll', function() {
        const currentScroll = window.pageYOffset;

        // Hiệu ứng ẩn khi cuộn xuống, hiện khi cuộn lên
        if (currentScroll > lastScroll && currentScroll > scrollThreshold) {
            header.style.transform = 'translateY(-100%)';
        } else {
            header.style.transform = 'translateY(0)';
        }

        // Hiệu ứng thay đổi màu nền khi scroll
        if (currentScroll > 50) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }

        lastScroll = currentScroll;
    });

    // Float animation cho các món ăn
    const menuImages = document.querySelectorAll('.menu-image');
    menuImages.forEach(img => {
        img.style.animationDelay = img.parentElement.getAttribute('data-delay');
    });

    // Intersection Observer cho các animation
    const animateElements = document.querySelectorAll('.slide-in-left, .slide-in-right, .fade-in');

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animated');
            }
        });
    }, { threshold: 0.1 });

    animateElements.forEach(el => observer.observe(el));

    // Animation cho logo
    const logo = document.querySelector('.logo-img');
    logo.addEventListener('mouseover', () => {
        logo.style.animation = 'pulse 0.5s ease';
    });

    logo.addEventListener('animationend', () => {
        logo.style.animation = '';
    });
});