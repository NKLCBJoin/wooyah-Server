CREATE TABLE `users` (
                         `user_id` BIGINT NOT NULL,
                         `nickname` VARCHAR(15) NOT NULL,
                         `profile_image` VARCHAR(200) NULL,
                         `location` VARCHAR(200) NOT NULL,
                         `phone` VARCHAR(20) NOT NULL,
                         `gender` VARCHAR(6) NULL CHECK (gender IN ('MAN', 'WOMAN')),
                         `device_number` VARCHAR(64) NOT NULL,
                         `latitude` DECIMAL(9,6) NULL,
                         `longitude` DECIMAL(9,6) NULL,
                         `created_at` DATETIME NOT NULL,
                         PRIMARY KEY (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `products` (
                            `product_id` BIGINT NOT NULL,
                            `product_name` VARCHAR(50) NOT NULL,
                            PRIMARY KEY (`product_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `carts` (
                         `cart_id` BIGINT NOT NULL,
                         `shopping_location` VARCHAR(200) NOT NULL,
                         `participant_number` INT NULL,
                         `latitude` DECIMAL(9,6) NOT NULL,
                         `longitude` DECIMAL(9,6) NOT NULL,
                         `status` VARCHAR(32) NOT NULL CHECK (status IN ('INPROGRESS', 'EXPIRED')),
                         `created_at` DATETIME NOT NULL,
                         PRIMARY KEY (`cart_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `cart_products` (
                                 `cart_product_id` BIGINT NOT NULL,
                                 `cart_id` BIGINT NOT NULL,
                                 `product_id` BIGINT NOT NULL,
                                 PRIMARY KEY (`cart_product_id`),
                                 FOREIGN KEY (`cart_id`) REFERENCES `carts`(`cart_id`) ON DELETE CASCADE,
                                 FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `cart_users` (
                              `cart_user_id` BIGINT NOT NULL,
                              `user_id` BIGINT NOT NULL,
                              `cart_id` BIGINT NOT NULL,
                              `is_owner` BOOLEAN NOT NULL,
                              `status` VARCHAR(32) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
                              PRIMARY KEY (`cart_user_id`),
                              FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`),
                              FOREIGN KEY (`cart_id`) REFERENCES `carts`(`cart_id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
