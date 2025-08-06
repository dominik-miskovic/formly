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
('Link', 'pass1'), ('Zelda', 'pass2'), ('Ganondorf', 'pass3'), ('Impa', 'pass4'), ('Sidon', 'pass5'),
('Riju', 'pass6'), ('Tulin', 'pass7'), ('Yunobo', 'pass8'), ('Mipha', 'pass9'), ('Daruk', 'pass10');

-- Generate 10 Posts by the users with varied timestamps
-- Timestamps will range from ~11 months ago to very recent.

-- Posts from ~11 months ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
('My adventures in Hyrule', 'Just defeated a band of Bokoblins near Hyrule Ridge. The Master Sword feels heavier today.', 1, DATEADD('MONTH', -11, CURRENT_TIMESTAMP), DATEADD('MONTH', -11, CURRENT_TIMESTAMP)),
('Royal Research Notes', 'Studying the ancient Sheikah technology. The Divine Beasts are a marvel of engineering.', 2, DATEADD('MONTH', -10, CURRENT_TIMESTAMP), DATEADD('MONTH', -10, CURRENT_TIMESTAMP));

-- Posts from ~7 months ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
('A New Era of Power', 'The Triforce of Power is the only thing that matters. Soon, Hyrule will kneel before me.', 3, DATEADD('MONTH', -7, CURRENT_TIMESTAMP), DATEADD('MONTH', -7, CURRENT_TIMESTAMP)),
('Horse Riding Techniques', 'Epona and I explored the Faron grasslands today. A good horse is essential for any hero.', 1, DATEADD('MONTH', -6, CURRENT_TIMESTAMP), DATEADD('MONTH', -6, CURRENT_TIMESTAMP));

-- Posts from ~3 months ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
('Whispers of the Sheikah', 'The signs are clear. Calamity Ganon will return. We must be prepared to guide the hero.', 4, DATEADD('MONTH', -3, CURRENT_TIMESTAMP), DATEADD('MONTH', -3, CURRENT_TIMESTAMP)),
('Zora''s Domain Bulletin', 'The waters are calm and our domain is prospering. I invite all Hylians to visit our beautiful home.', 5, DATEADD('MONTH', -3, DATEADD('DAY', -5, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -5, CURRENT_TIMESTAMP))); -- Slightly different day

-- Posts from ~1 month ago
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
('Gerudo Town Defenses', 'Our patrols have been strengthened. No male voe shall enter Gerudo Town without permission. And watch out for the Moldugas.', 6, DATEADD('MONTH', -1, CURRENT_TIMESTAMP), DATEADD('MONTH', -1, CURRENT_TIMESTAMP)),
('From the Rito Roost', 'The wind carries tales from all over Hyrule. I saw the Divine Beast Vah Medoh from the top of the roost today.', 7, DATEADD('MONTH', -1, DATEADD('DAY', 3, CURRENT_TIMESTAMP)), DATEADD('MONTH', -1, DATEADD('DAY', 3, CURRENT_TIMESTAMP)));

-- Recent Posts (last few days)
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES
('Goron Gourmet', 'Rock Roast is the best! Nothing beats a good meal after a long day of mining prime ore.', 8, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
('Capturing Hyrule''s Beauty', 'Using the Sheikah Slate to document the flora and fauna of Hyrule. The Silent Princess is particularly hard to find.', 2, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP));


-- Generate Comments with varied timestamps, following their respective posts
-- Assuming post_id 1-10 correspond to the posts inserted above in order.

-- Comments for Post 1: 'My adventures in Hyrule' (by Link)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('Stay safe, Link! Hyrule depends on you.', 1, 2, DATEADD('MONTH', -11, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -11, DATEADD('HOUR', 1, CURRENT_TIMESTAMP))),
('Foolish hero. Your luck will run out.', 1, 3, DATEADD('MONTH', -11, DATEADD('DAY', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -11, DATEADD('DAY', 1, CURRENT_TIMESTAMP))),
('The Princess is worried. Do not be reckless.', 1, 4, DATEADD('MONTH', -11, DATEADD('DAY', 2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -11, DATEADD('DAY', 2, CURRENT_TIMESTAMP)));

-- Comments for Post 2: 'Royal Research Notes' (by Zelda)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('Your research is our best hope, Zelda. Let me know if you need anything.', 2, 1, DATEADD('MONTH', -10, DATEADD('MINUTE', 30, CURRENT_TIMESTAMP)), DATEADD('MONTH', -10, DATEADD('MINUTE', 30, CURRENT_TIMESTAMP))),
('The old texts speak of this technology. It is wise to study them.', 2, 4, DATEADD('MONTH', -10, DATEADD('HOUR', 5, CURRENT_TIMESTAMP)), DATEADD('MONTH', -10, DATEADD('HOUR', 5, CURRENT_TIMESTAMP))),
('Fascinating! The Zora architecture also uses advanced principles. Perhaps we could compare notes.', 2, 5, DATEADD('MONTH', -9, DATEADD('DAY', -20, CURRENT_TIMESTAMP)), DATEADD('MONTH', -9, DATEADD('DAY', -20, CURRENT_TIMESTAMP))),
('Toys for a powerless princess.', 2, 3, DATEADD('MONTH', -9, DATEADD('DAY', -15, CURRENT_TIMESTAMP)), DATEADD('MONTH', -9, DATEADD('DAY', -15, CURRENT_TIMESTAMP)));

-- Comments for Post 3: 'A New Era of Power' (by Ganondorf)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('Your ambition will be your downfall, Ganondorf.', 3, 6, DATEADD('MONTH', -7, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -7, DATEADD('HOUR', 2, CURRENT_TIMESTAMP))),
('I will stop you.', 3, 1, DATEADD('MONTH', -7, DATEADD('HOUR', 3, CURRENT_TIMESTAMP)), DATEADD('MONTH', -7, DATEADD('HOUR', 3, CURRENT_TIMESTAMP))),
('Hyrule will never bow to you.', 3, 2, DATEADD('MONTH', -7, DATEADD('HOUR', 4, CURRENT_TIMESTAMP)), DATEADD('MONTH', -7, DATEADD('HOUR', 4, CURRENT_TIMESTAMP)));

-- Comments for Post 4: 'Horse Riding Techniques' (by Link)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('Epona is a fine horse! Maybe we could race sometime?', 4, 7, DATEADD('MONTH', -6, DATEADD('DAY', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -6, DATEADD('DAY', 1, CURRENT_TIMESTAMP))),
('A horse is good, but rolling down a mountain is faster! Goro!', 4, 8, DATEADD('MONTH', -6, DATEADD('DAY', 2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -6, DATEADD('DAY', 2, CURRENT_TIMESTAMP))),
('I remember riding with you. Cherished memories.', 4, 9, DATEADD('MONTH', -5, DATEADD('DAY', -15, CURRENT_TIMESTAMP)), DATEADD('MONTH', -5, DATEADD('DAY', -15, CURRENT_TIMESTAMP))),
('A good steed is half the battle, little guy!', 4, 10, DATEADD('MONTH', -5, DATEADD('DAY', -10, CURRENT_TIMESTAMP)), DATEADD('MONTH', -5, DATEADD('DAY', -10, CURRENT_TIMESTAMP)));

-- Comments for Post 5: 'Whispers of the Sheikah' (by Impa)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('I feel it too, Impa. A darkness is growing.', 5, 2, DATEADD('MONTH', -3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP))),
('I am ready.', 5, 1, DATEADD('MONTH', -3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)));


-- Comments for Post 6: 'Zora''s Domain Bulletin' (by Sidon)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('Zora''s Domain is truly a sight to behold. Thank you for the invitation, my friend!', 6, 1, DATEADD('MONTH', -3, DATEADD('DAY', -4, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -4, CURRENT_TIMESTAMP))),
('I would love to visit soon, Prince Sidon.', 6, 2, DATEADD('MONTH', -3, DATEADD('DAY', -3, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -3, CURRENT_TIMESTAMP))),
('My dear brother, you''ve grown into a fine leader.', 6, 9, DATEADD('MONTH', -3, DATEADD('DAY', -2, CURRENT_TIMESTAMP)), DATEADD('MONTH', -3, DATEADD('DAY', -2, CURRENT_TIMESTAMP)));

-- Comments for Post 7: 'Gerudo Town Defenses' (by Riju)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('The Gerudo are strong, but you are naive to think you can keep me out.', 7, 3,
 DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP))),
 DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)))
), -- End of row 1
('Your leadership does honor to your ancestors, Chief Riju.', 7, 4,
 DATEADD('MONTH', -1, DATEADD('DAY', 4, CURRENT_TIMESTAMP)),
 DATEADD('MONTH', -1, DATEADD('DAY', 4, CURRENT_TIMESTAMP))
), -- End of row 2
('The desert heat is intense. Stay vigilant!', 7, 5,
 DATEADD('DAY', -20, CURRENT_TIMESTAMP),
 DATEADD('DAY', -20, CURRENT_TIMESTAMP)
), -- End of row 3
('I had to dress as a vai to get in, but your city is amazing. The sand seals are fast!', 7, 1,
 DATEADD('DAY', -15, CURRENT_TIMESTAMP),
 DATEADD('DAY', -15, CURRENT_TIMESTAMP)
), -- End of row 4
('Stay strong, Riju. You have my full support.', 7, 2,
 DATEADD('DAY', -10, CURRENT_TIMESTAMP),
 DATEADD('DAY', -10, CURRENT_TIMESTAMP)
); -- End of row 5 and statement

-- Comments for Post 8: 'From the Rito Roost' (by Tulin)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('The view must be incredible from up there!', 8, 6, DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP))), DATEADD('MONTH', -1, DATEADD('DAY', 3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)))),
('I can see you from Death Mountain! Goro!', 8, 8, DATEADD('MONTH', -1, DATEADD('DAY', 5, CURRENT_TIMESTAMP)), DATEADD('MONTH', -1, DATEADD('DAY', 5, CURRENT_TIMESTAMP)));

-- Comments for Post 9: 'Goron Gourmet' (by Yunobo)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('A Goron''s appetite is legendary.', 9, 9, DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
('That''s my boy! Nothing like a prime Rock Roast!', 9, 10, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
('I tried it once. It''s... an acquired taste.', 9, 1, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP));

-- Comments for Post 10: 'Capturing Hyrule''s Beauty' (by Zelda)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES
('You''ve captured the beauty of Hyrule perfectly, Zelda.', 10, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
('A valuable record for future generations.', 10, 4, DATEADD('DAY', -1, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)), DATEADD('DAY', -1, DATEADD('HOUR', 2, CURRENT_TIMESTAMP))),
('Perhaps you could take a picture of Zora''s Domain next? For the archives!', 10, 5, DATEADD('DAY', 0, DATEADD('HOUR', -2, CURRENT_TIMESTAMP)), DATEADD('DAY', 0, DATEADD('HOUR', -2, CURRENT_TIMESTAMP)));

-- Generate Votes (timestamps for votes should be after the post/comment they are for)
-- voteType: 1 for upvote, -1 for downvote

-- Votes for Posts
INSERT INTO vote (user_id, post_id, comment_id, vote_type, created_at) VALUES
(1, 2, NULL, 1, DATEADD('MONTH', -10, DATEADD('DAY', 1, CURRENT_TIMESTAMP))), -- User 1 upvotes Post 2
(1, 3, NULL, -1, DATEADD('MONTH', -7, DATEADD('DAY', 1, CURRENT_TIMESTAMP))), -- User 1 downvotes Post 3
(2, 1, NULL, 1, DATEADD('MONTH', -11, DATEADD('DAY', 1, CURRENT_TIMESTAMP))),
(3, 1, NULL, -1, DATEADD('MONTH', -11, DATEADD('DAY', 2, CURRENT_TIMESTAMP))), -- Ganondorf downvotes Link's post
(4, 1, NULL, 1, DATEADD('MONTH', -10, DATEADD('DAY', -5, CURRENT_TIMESTAMP))),
(5, 2, NULL, 1, DATEADD('MONTH', -9, CURRENT_TIMESTAMP)), -- Sidon upvotes Zelda's post
(6, 3, NULL, -1, DATEADD('MONTH', -6, CURRENT_TIMESTAMP)), -- Riju downvotes Ganondorf's post
(7, 4, NULL, 1, DATEADD('MONTH', -5, CURRENT_TIMESTAMP)),
(8, 5, NULL, 1, DATEADD('MONTH', -2, CURRENT_TIMESTAMP)),
(9, 6, NULL, 1, DATEADD('MONTH', -2, DATEADD('DAY', -10, CURRENT_TIMESTAMP))), -- Mipha upvotes Sidon's post
(10, 7, NULL, 1, DATEADD('MONTH', -1, DATEADD('DAY', 5, CURRENT_TIMESTAMP))),
(1, 8, NULL, 1, DATEADD('MONTH', -1, DATEADD('DAY', 6, CURRENT_TIMESTAMP))),
(2, 9, NULL, 1, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
(3, 10, NULL, -1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)), -- Ganondorf downvotes Zelda's photography
(4, 5, NULL, 1, DATEADD('MONTH', -1, CURRENT_TIMESTAMP)),
(5, 6, NULL, 1, DATEADD('MONTH', -1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)));

-- Votes for Comments
-- Comment IDs are assumed to be sequential. This part remains highly dependent on actual comment IDs.
INSERT INTO vote (user_id, post_id, comment_id, vote_type, created_at) VALUES
-- Votes for comments on Post 1 (Link's adventures)
(2, NULL, 2, -1, DATEADD('MONTH', -11, DATEADD('DAY', 1, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)))), -- Zelda downvotes Ganondorf's comment on Link's post
(4, NULL, 1, 1, DATEADD('MONTH', -11, DATEADD('HOUR', 2, CURRENT_TIMESTAMP))), -- Impa upvotes Zelda's comment on Link's post
(2, NULL, 3, 1, DATEADD('MONTH', -11, DATEADD('DAY', 2, CURRENT_TIMESTAMP))), -- Zelda upvotes Impa's comment on Link's post

-- Votes for comments on Post 2 (Zelda's research)
(1, NULL, 4, 1, DATEADD('MONTH', -10, DATEADD('MINUTE', 45, CURRENT_TIMESTAMP))), -- Link upvotes his comment on Zelda's post
(3, NULL, 4, -1, DATEADD('MONTH', -9, DATEADD('DAY', -14, CURRENT_TIMESTAMP))), -- Ganondorf downvotes Link's comment on Zelda's post
(1, NULL, 6, 1, DATEADD('MONTH', -9, DATEADD('DAY', -19, CURRENT_TIMESTAMP))), -- Link upvotes Sidon's comment on Zelda's post
(9, NULL, 6, 1, DATEADD('MONTH', -9, DATEADD('DAY', -19, CURRENT_TIMESTAMP))), -- Mipha also upvotes Sidon's comment

-- Votes for comments on Post 3 (Ganondorf's power)
(1, NULL, 8, 1, DATEADD('MONTH', -7, DATEADD('HOUR', 5, CURRENT_TIMESTAMP))), -- Link upvotes Riju's comment against Ganondorf
(2, NULL, 9, 1, DATEADD('MONTH', -7, DATEADD('HOUR', 5, CURRENT_TIMESTAMP))), -- Zelda upvotes Link's comment against Ganondorf
(6, NULL, 10, 1, DATEADD('MONTH', -7, DATEADD('HOUR', 6, CURRENT_TIMESTAMP))), -- Riju upvotes Zelda's comment against Ganondorf

-- Votes for comments on Post 4 (Link's horse)
(10, NULL, 12, 1, DATEADD('MONTH', -5, DATEADD('DAY', -9, CURRENT_TIMESTAMP))), -- Daruk upvotes Yunobo's comment on Link's post
(8, NULL, 14, 1, DATEADD('MONTH', -5, DATEADD('DAY', -9, CURRENT_TIMESTAMP))), -- Yunobo upvotes Daruk's comment
(5, NULL, 13, 1, DATEADD('MONTH', -5, DATEADD('DAY', -14, CURRENT_TIMESTAMP))), -- Sidon upvotes Mipha's comment
(10, NULL, 11, 1, DATEADD('MONTH', -6, DATEADD('DAY', 2, CURRENT_TIMESTAMP))), -- Daruk upvotes Tulin's comment

-- Votes for comments on Post 5 (Impa's whispers)
(4, NULL, 15, 1, DATEADD('MONTH', -3, DATEADD('HOUR', 2, CURRENT_TIMESTAMP))), -- Impa upvotes Zelda's reply to her post

-- Votes for comments on Post 6 (Sidon's bulletin)
(3, NULL, 19, -1, DATEADD('MONTH', -3, DATEADD('DAY', -1, CURRENT_TIMESTAMP))), -- Ganondorf downvotes Mipha's comment to Sidon

-- Votes for comments on Post 8 (Tulin's roost)
(7, NULL, 25, 1, DATEADD('MONTH', -1, DATEADD('DAY', 4, CURRENT_TIMESTAMP))), -- Tulin upvotes Riju's comment on his post

-- Votes for comments on Post 9 (Yunobo's gourmet)
(1, NULL, 29, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP)), -- Link upvotes his own comment about Rock Roast (he's not good at cooking)

-- Votes for comments on Post 10 (Zelda's photography)
(1, NULL, 31, 1, DATEADD('DAY', -1, DATEADD('HOUR', 3, CURRENT_TIMESTAMP))), -- Link upvotes Impa's comment on Zelda's post
(5, NULL, 30, 1, DATEADD('DAY', -1, DATEADD('HOUR', 1, CURRENT_TIMESTAMP))); -- Sidon upvotes Link's comment on Zelda's post