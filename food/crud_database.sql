-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 04, 2025 lúc 01:22 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `crud_database`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `authorities`
--

CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Đang đổ dữ liệu cho bảng `authorities`
--

INSERT INTO `authorities` (`username`, `authority`) VALUES
('hung', 'ROLE_ADMIN'),
('hung', 'ROLE_MANAGER'),
('hung', 'ROLE_USER'),
('test', 'ROLE_MANAGER'),
('test', 'ROLE_USER'),
('tru', 'ROLE_USER'),
('truong', 'ROLE_MANAGER'),
('truong', 'ROLE_USER');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customers`
--

CREATE TABLE `customers` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` text NOT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `customers`
--

INSERT INTO `customers` (`id`, `name`, `email`, `phone`, `address`, `created_at`, `updated_at`) VALUES
(1, 'binh', 'binhhx5@gmail.com', '0356752736', 'binhdinh', '2025-03-27 04:27:24', '2025-03-27 04:27:24'),
(2, 'binh1', 'binhhx4@gmail.com', '0356752736', 'binhdinh', '2025-03-27 06:24:45', '2025-03-27 06:24:45'),
(3, 'binh tran', 'binhhx2@gmail.com', '0356752736', 'binh dinh', '2025-03-27 16:39:33', '2025-03-27 16:39:33');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `employee`
--

CREATE TABLE `employee` (
  `id` int(11) NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Đang đổ dữ liệu cho bảng `employee`
--

INSERT INTO `employee` (`id`, `first_name`, `last_name`, `email`) VALUES
(1, 'Leslie', 'Andrews', 'leslie@luv2code.com'),
(2, 'Emma', 'Baumgarten', 'emma@luv2code.com'),
(3, 'Avani', 'Gupta', 'avani@luv2code.com'),
(4, 'Yuri', 'Petrov', 'yuri@luv2code.com'),
(5, 'Juan', 'Vega', 'juan@luv2code.com');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `category` enum('GANUONG','GAHAP','COMBO','KHAC') NOT NULL,
  `spicy_level` int(11) DEFAULT 0,
  `is_featured` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `product`
--

INSERT INTO `product` (`id`, `name`, `description`, `price`, `image`, `category`, `spicy_level`, `is_featured`, `created_at`, `updated_at`) VALUES
(1, 'Gà rán truyền thống', 'ggg', 7500000.00, 'suon.png', 'KHAC', 1, 1, '2025-03-26 23:54:51', '2025-03-30 02:09:31'),
(2, 'Gà sốt cay Hàn Quốc', 'Gà rán sốt cay chuẩn vị Hàn, độ cay 3/5', 89000.00, 'Background2.png', 'GANUONG', 3, 1, '2025-03-26 23:54:51', '2025-03-27 02:47:38'),
(3, 'Gà nướng mật ong', 'Gà nướng thơm mùi mật ong', 110000.00, 'nuong.png', 'GAHAP', 0, 0, '2025-03-26 23:54:51', '2025-03-27 02:48:27'),
(4, 'Combo gia đình', '3 gà rán + 2 khoai tây + 2 pepsi', 220000.00, 'Background2.png', 'COMBO', 0, 1, '2025-03-26 23:54:51', '2025-03-27 02:48:15'),
(10, '123', '123', 123.00, '1743068975441_a1.jpg', 'GANUONG', 0, 0, '2025-03-27 16:49:35', '2025-03-27 16:49:35'),
(11, 'binh', 'ngon', 4123213.00, '1743069767674_4k-ig-kaisa-chroma-splash-art-v0-5fs46tasgeub1.webp', 'GAHAP', 0, 0, '2025-03-27 17:02:47', '2025-03-27 17:02:47'),
(12, 'binh', '123', 123.00, '1743070158869_game1.jpg', 'GANUONG', 0, 0, '2025-03-27 17:09:18', '2025-03-27 17:09:18');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`username`, `password`, `enabled`) VALUES
('hung', '{bcrypt}$2a$12$lSMPUB8w1HAly9r0ScoO2esroXkYvUfADSWGkT0sx3GG7e7cwL5Da', 1),
('test', '{bcrypt}$2a$10$ML5A8g1CiJGkQ7dZHCnZ2euL.4BPQoxdD.T0.NSGThu2hu52UQrvu', 1),
('tru', '{bcrypt}$2a$12$B7vdNvAEDSOJTiAA5RFj3uqwZMl7rZ45etqpczZLSEbvOt3Rw5MRu', 1),
('truong', '{bcrypt}$2a$12$xaISPBSgJqLmMhgpdbO9OOZjnk2ItBvN.sP.bxnp4PAIRZpsYZstC', 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `authorities`
--
ALTER TABLE `authorities`
  ADD UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  ADD UNIQUE KEY `uk_username_authority` (`username`,`authority`);

--
-- Chỉ mục cho bảng `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Chỉ mục cho bảng `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `customers`
--
ALTER TABLE `customers`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `authorities`
--
ALTER TABLE `authorities`
  ADD CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
