/* ============================================================
   Ocean View Resort - Main JavaScript
   ============================================================ */

// ============ Navbar Scroll Effect ============
window.addEventListener('scroll', function () {
    const navbar = document.getElementById('mainNav');
    if (navbar) {
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    }
});

// ============ Mobile Nav Toggle ============
function toggleMobileNav() {
    const navLinks = document.getElementById('navLinks');
    if (navLinks) {
        navLinks.classList.toggle('active');
    }
}

// ============ Dark Mode ============
function toggleDarkMode() {
    const body = document.documentElement;
    const icon = document.getElementById('darkModeIcon');

    if (body.getAttribute('data-theme') === 'dark') {
        body.removeAttribute('data-theme');
        if (icon) icon.className = 'fas fa-moon';
        localStorage.setItem('theme', 'light');
    } else {
        body.setAttribute('data-theme', 'dark');
        if (icon) icon.className = 'fas fa-sun';
        localStorage.setItem('theme', 'dark');
    }
}

// Load saved theme
(function () {
    const saved = localStorage.getItem('theme');
    if (saved === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
        const icon = document.getElementById('darkModeIcon');
        if (icon) icon.className = 'fas fa-sun';
    }
})();

// ============ Accordion Toggle ============
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.accordion-header').forEach(function (header) {
        header.addEventListener('click', function () {
            const item = this.parentElement;
            const isActive = item.classList.contains('active');

            // Close all
            document.querySelectorAll('.accordion-item').forEach(function (i) {
                i.classList.remove('active');
            });

            // Toggle current
            if (!isActive) {
                item.classList.add('active');
            }
        });
    });
});

// ============ AJAX Room Availability Check ============
function checkAvailability() {
    const roomType = document.getElementById('roomType');
    const checkIn = document.getElementById('checkIn');
    const checkOut = document.getElementById('checkOut');
    const resultDiv = document.getElementById('availabilityResult');

    if (!roomType || !checkIn || !checkOut || !resultDiv) return;

    if (!roomType.value || !checkIn.value || !checkOut.value) {
        resultDiv.innerHTML = '<div class="alert-custom alert-warning"><i class="fas fa-exclamation-triangle"></i> Please select room type and dates.</div>';
        return;
    }

    const ctx = document.querySelector('meta[name="ctx"]');
    const basePath = ctx ? ctx.getAttribute('content') : '';

    fetch(basePath + '/api/rooms/available?type=' + encodeURIComponent(roomType.value) +
        '&checkIn=' + checkIn.value + '&checkOut=' + checkOut.value)
        .then(function (response) { return response.json(); })
        .then(function (data) {
            if (data.available) {
                resultDiv.innerHTML =
                    '<div class="alert-custom alert-success">' +
                    '<i class="fas fa-check-circle"></i>' +
                    '<div>' +
                    '<strong>' + data.count + ' room(s) available!</strong><br>' +
                    '<span style="font-size: 0.85rem;">' + data.nights + ' night(s) × $' + data.pricePerNight.toFixed(2) + '</span><br>' +
                    '<span style="font-size: 0.85rem;">Tax: $' + data.tax.toFixed(2) + ' | Service: $' + data.serviceCharge.toFixed(2) + '</span><br>' +
                    '<strong style="color: var(--accent); font-size: 1.1rem;">Total: $' + data.totalAmount.toFixed(2) + '</strong>' +
                    '</div></div>';
            } else {
                resultDiv.innerHTML =
                    '<div class="alert-custom alert-danger"><i class="fas fa-times-circle"></i> No rooms available for the selected dates and type.</div>';
            }
        })
        .catch(function (err) {
            resultDiv.innerHTML = '<div class="alert-custom alert-danger"><i class="fas fa-exclamation-circle"></i> Error checking availability.</div>';
        });
}

// ============ Room Price & Preview Update ============
function updateRoomPrice() {
    const roomType = document.getElementById('roomType');
    const priceDisplay = document.getElementById('pricePerNight');
    const previewDiv = document.getElementById('roomPreview');
    const previewImg = document.getElementById('previewImg');

    if (!roomType || !priceDisplay) return;

    const ctx = document.querySelector('meta[name="ctx"]');
    const basePath = ctx ? ctx.getAttribute('content') : '';

    // Update Image Preview
    if (roomType.value && previewDiv && previewImg) {
        const typeKey = roomType.value.toLowerCase();
        previewImg.src = basePath + '/images/room_' + typeKey + '.jpg';
        previewDiv.style.display = 'block';
    } else if (previewDiv) {
        previewDiv.style.display = 'none';
    }

    fetch(basePath + '/api/rooms/price?type=' + encodeURIComponent(roomType.value))
        .then(function (r) { return r.json(); })
        .then(function (data) {
            if (data.pricePerNight) {
                priceDisplay.textContent = '$' + data.pricePerNight.toFixed(2) + ' / night';
            }
        });
}

// ============ Form Validation Highlight ============
function validateField(input) {
    if (input.value.trim() === '' && input.required) {
        input.style.borderColor = '#E74C3C';
    } else {
        input.style.borderColor = '';
    }
}

// ============ Confirm Dialog ============
function confirmAction(message) {
    return confirm(message || 'Are you sure?');
}

// ============ Auto-dismiss alerts ============
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.alert-custom[data-dismiss]').forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            setTimeout(function () { alert.remove(); }, 500);
        }, 5000);
    });
});

// ============ Print Invoice ============
function printInvoice() {
    window.print();
}

// ============ Scroll Animations ============
(function initScrollAnimations() {
    const observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-fadeInUp');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.animate-on-scroll').forEach(function (el) {
            observer.observe(el);
        });
    });
})();
