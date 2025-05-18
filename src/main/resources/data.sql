-- Clear existing data (optional)
-- DELETE FROM vote;
-- DELETE FROM comment;
-- DELETE FROM post;
-- DELETE FROM app_user;

-- Reset sequences (optional)
-- ALTER TABLE app_user ALTER COLUMN id RESTART WITH 1;
-- ALTER TABLE post ALTER COLUMN id RESTART WITH 1;
-- ALTER TABLE comment ALTER COLUMN id RESTART WITH 1;
-- ALTER TABLE vote ALTER COLUMN id RESTART WITH 1;

-- Generate 10 Users (passwords are plain text for script simplicity)
INSERT INTO app_user (username, password) VALUES
                                              ('user1', 'pass1'), ('user2', 'pass2'), ('user3', 'pass3'), ('user4', 'pass4'), ('user5', 'pass5'),
                                              ('user6', 'pass6'), ('user7', 'pass7'), ('user8', 'pass8'), ('user9', 'pass9'), ('user10', 'pass10');

-- Generate 10 Posts by the users with varied timestamps
-- Timestamps will range from ~11 months ago to very recent.

-- Posts from ~11 months ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
                                                                       ('First Post Title!', 'This is the content of the very first post. Exciting stuff!', 1, DATEADD('MONTH', -11, CURRENT_TIMESTAMP), DATEADD('MONTH', -11, CURRENT_TIMESTAMP)),
                                                                       ('Exploring H2 Database', 'A deep dive into H2 features and capabilities for developers.', 2, DATEADD('MONTH', -10, CURRENT_TIMESTAMP), DATEADD('MONTH', -10, CURRENT_TIMESTAMP));

-- Posts from ~7 months ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
                                                                       ('Java Best Practices 2024', 'My thoughts on modern Java development patterns.', 3, DATEADD('MONTH', -7, CURRENT_TIMESTAMP), DATEADD('MONTH', -7, CURRENT_TIMESTAMP)),
                                                                       ('Learning Thymeleaf with Spring Boot', 'Tips and tricks for effective server-side rendering.', 1, DATEADD('MONTH', -6, CURRENT_TIMESTAMP), DATEADD('MONTH', -6, CURRENT_TIMESTAMP));

-- Posts from ~3 months ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
                                                                       ('The Future of Web Development', 'Predictions and trends to watch out for.', 4, DATEADD('MONTH', -3, CURRENT_TIMESTAMP), DATEADD('MONTH', -3, CURRENT_TIMESTAMP)),
                                                                       ('A Guide to Microservices', 'Understanding the architecture and when to use it.', 5, DATEADD('MONTH', -3, DATEADD('DAY', -5, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -5, CURRENT_TIMESTAMP))); -- Slightly different day

-- Posts from ~1 month ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
                                                                       ('Cooking Adventures: My Favorite Recipe', 'Sharing a delicious and easy recipe I love.', 6, DATEADD('MONTH', -1, CURRENT_TIMESTAMP), DATEADD('MONTH', -1, CURRENT_TIMESTAMP)),
                                                                       ('Travel Blog: My Trip to the Mountains', 'Recounting an amazing journey and beautiful landscapes.', 7, DATEADD('MONTH', -1, DATEADD('DAY', 3, CURRENT_TIMESTAMP)), DATEADD('MONTH', -1, DATEADD('DAY', 3, CURRENT_TIMESTAMP)));

-- Recent Posts (last few days)
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
                                                                       ('Book Review: Sci-Fi Classic', 'My review of a timeless science fiction novel.', 8, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
                                                                       ('Photography Tips for Beginners', 'How to take better photos with any camera.', 2, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP));


-- Generate Comments with varied timestamps, following their respective posts
-- Assuming post_id 1-10 correspond to the posts inserted above in order.

-- Comments for Post 1 (created ~11 months ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('Great first post!', 1, 2, DATEADD('MONTH', -11, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -11, DATEADD('HOUR', 1, CURRENT_TIMESTAMP))),
                                                                            ('Looking forward to more content from you.', 1, 3, DATEADD('MONTH', -11, DATEADD('DAY', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -11, DATEADD('DAY', 1, CURRENT_TIMESTAMP)));

-- Comments for Post 2 (created ~10 months ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('H2 is indeed very useful for testing.', 2, 1, DATEADD('MONTH', -10, DATEADD('MINUTE', 30, CURRENT_TIMESTAMP)), DATEADD('MONTH', -10, DATEADD('MINUTE', 30, CURRENT_TIMESTAMP))),
                                                                            ('Thanks for sharing these insights!', 2, 4, DATEADD('MONTH', -10, DATEADD('HOUR', 5, CURRENT_TIMESTAMP)), DATEADD('MONTH', -10, DATEADD('HOUR', 5, CURRENT_TIMESTAMP))),
                                                                            ('Any specific H2 features you find most valuable?', 2, 5, DATEADD('MONTH', -9, DATEADD('DAY', -20, CURRENT_TIMESTAMP)), DATEADD('MONTH', -9, DATEADD('DAY', -20, CURRENT_TIMESTAMP))); -- A later comment

-- Comments for Post 3 (created ~7 months ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
    ('Solid advice on Java patterns.', 3, 6, DATEADD('MONTH', -7, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -7, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)));

-- Comments for Post 4 (created ~6 months ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('Thymeleaf can be tricky sometimes, good tips!', 4, 7, DATEADD('MONTH', -6, DATEADD('DAY', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -6, DATEADD('DAY', 1, CURRENT_TIMESTAMP))),
                                                                            ('I prefer client-side rendering, but this is interesting.', 4, 8, DATEADD('MONTH', -6, DATEADD('DAY', 2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -6, DATEADD('DAY', 2, CURRENT_TIMESTAMP))),
                                                                            ('Have you tried using Thymeleaf Layout Dialect?', 4, 9, DATEADD('MONTH', -5, DATEADD('DAY', -15, CURRENT_TIMESTAMP)), DATEADD('MONTH', -5, DATEADD('DAY', -15, CURRENT_TIMESTAMP))),
                                                                            ('Nice article!', 4, 10, DATEADD('MONTH', -5, DATEADD('DAY', -10, CURRENT_TIMESTAMP)), DATEADD('MONTH', -5, DATEADD('DAY', -10, CURRENT_TIMESTAMP)));

-- Comments for Post 6 (created ~3 months ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('Microservices are complex but powerful.', 6, 1, DATEADD('MONTH', -3, DATEADD('DAY', -4, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -4, CURRENT_TIMESTAMP))),
                                                                            ('Good overview!', 6, 2, DATEADD('MONTH', -3, DATEADD('DAY', -3, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -3, CURRENT_TIMESTAMP)));

-- Comments for Post 7 (created ~1 month ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('Yum, that recipe sounds great!', 7, 3,
                                                                             DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP))),
                                                                             DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)))
                                                                            ), -- End of row 1
                                                                            ('I will try this weekend.', 7, 4,
                                                                             DATEADD('MONTH', -1, DATEADD('DAY', 4, CURRENT_TIMESTAMP)),
                                                                             DATEADD('MONTH', -1, DATEADD('DAY', 4, CURRENT_TIMESTAMP))
                                                                            ), -- End of row 2
                                                                            ('Do you have nutritional information?', 7, 5,
                                                                             DATEADD('DAY', -20, CURRENT_TIMESTAMP),  -- This was the line with the error
                                                                             DATEADD('DAY', -20, CURRENT_TIMESTAMP)   -- This is its corresponding updated_at
                                                                            ), -- End of row 3
                                                                            ('Thanks for sharing!', 7, 1,
                                                                             DATEADD('DAY', -15, CURRENT_TIMESTAMP),
                                                                             DATEADD('DAY', -15, CURRENT_TIMESTAMP)
                                                                            ), -- End of row 4
                                                                            ('Looks delicious!', 7, 2,
                                                                             DATEADD('DAY', -10, CURRENT_TIMESTAMP),
                                                                             DATEADD('DAY', -10, CURRENT_TIMESTAMP)
                                                                            ); -- End of row 5 and statement

-- Comments for Post 8 (created ~1 month ago, few days after post 7)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('Wow, those mountains look stunning!', 8, 6, DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP))), DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)))),
                                                                            ('Added to my travel list!', 8, 8, DATEADD('MONTH', -1, DATEADD('DAY', 5, CURRENT_TIMESTAMP)), DATEADD('MONTH', -1, DATEADD('DAY', 5, CURRENT_TIMESTAMP)));

-- Comments for Post 9 (created ~5 days ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
                                                                            ('I love that book too!', 9, 9, DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
                                                                            ('A true classic indeed.', 9, 10, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- Comments for Post 10 (created ~2 days ago)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
    ('Great tips for beginners!', 10, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- Generate Votes (timestamps for votes should be after the post/comment they are for)
-- voteType: 1 for upvote, -1 for downvote

-- Votes for Posts
INSERT INTO vote (user_id, post_id, comment_id, vote_type, created_at) VALUES
                                                                           (1, 2, NULL, 1, DATEADD('MONTH', -10, DATEADD('DAY', 1, CURRENT_TIMESTAMP))), -- User 1 upvotes Post 2 (created ~10m ago)
                                                                           (1, 3, NULL, -1, DATEADD('MONTH', -7, DATEADD('DAY', 1, CURRENT_TIMESTAMP))), -- User 1 downvotes Post 3 (created ~7m ago)
                                                                           (2, 1, NULL, 1, DATEADD('MONTH', -11, DATEADD('DAY', 1, CURRENT_TIMESTAMP))),
                                                                           (3, 1, NULL, 1, DATEADD('MONTH', -11, DATEADD('DAY', 2, CURRENT_TIMESTAMP))),
                                                                           (4, 1, NULL, 1, DATEADD('MONTH', -10, DATEADD('DAY', -5, CURRENT_TIMESTAMP))),
                                                                           (5, 2, NULL, -1, DATEADD('MONTH', -9, CURRENT_TIMESTAMP)),
                                                                           (6, 3, NULL, 1, DATEADD('MONTH', -6, CURRENT_TIMESTAMP)),
                                                                           (7, 4, NULL, 1, DATEADD('MONTH', -5, CURRENT_TIMESTAMP)),
                                                                           (8, 5, NULL, 1, DATEADD('MONTH', -2, CURRENT_TIMESTAMP)),
                                                                           (9, 6, NULL, -1, DATEADD('MONTH', -2, DATEADD('DAY', -10, CURRENT_TIMESTAMP))),
                                                                           (10, 7, NULL, 1, DATEADD('MONTH', -1, DATEADD('DAY', 5, CURRENT_TIMESTAMP))),
                                                                           (1, 8, NULL, 1, DATEADD('MONTH', -1, DATEADD('DAY', 6, CURRENT_TIMESTAMP))),
                                                                           (2, 9, NULL, 1, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
                                                                           (3, 10, NULL, -1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
                                                                           (4, 5, NULL, 1, DATEADD('MONTH', -1, CURRENT_TIMESTAMP)),
                                                                           (5, 6, NULL, 1, DATEADD('MONTH', -1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)));

-- Votes for Comments
-- Assuming comment IDs are generated sequentially starting from 1.
-- This part remains highly dependent on actual comment IDs.
-- For simplicity, I'll make vote timestamps relative to CURRENT_TIMESTAMP,
-- assuming comments exist by then (which they should if script runs top-to-bottom).
-- A more robust way would be to tie vote timestamps more closely to their target comment's creation time.

-- Let C_ID be the placeholder for actual comment IDs.
-- Example for a few comments (you'd need to get actual generated comment IDs for accuracy)
INSERT INTO vote (user_id, post_id, comment_id, vote_type, created_at) VALUES
                                                                           (1, NULL, 1, 1, DATEADD('DAY', -300, CURRENT_TIMESTAMP)),  -- Vote on an old comment (assuming Comment ID 1 is old)
                                                                           (1, NULL, 3, -1, DATEADD('DAY', -250, CURRENT_TIMESTAMP)),
                                                                           (2, NULL, 2, 1, DATEADD('DAY', -280, CURRENT_TIMESTAMP)),
                                                                           (10, NULL, 22, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)); -- Vote on a recent comment (assuming Comment ID 22 is recent)
-- ... add more votes for comments as needed, adjusting comment_id and created_at ...
-- It's hard to make these perfectly "random" and "correct" without knowing generated IDs.
-- The key is that vote.created_at should be >= comment.created_at for that comment_id.