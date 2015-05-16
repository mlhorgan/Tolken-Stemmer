# Tolken-Stemmer

Part A

Implement a tokenization system.

Implement tokenization
Consider abbreviations such as "U.S.A." as one term: "USA" -- there are equivalents in p2-input-part-A.txt
All other punctuation should be considered word separators: "200,000" should become ["200", "000"]
Lowercase all letters.
Libraries are not allowed for this part.
Implement stopword removal
Use the Lemur-418 stopword list
Implement the first two steps of Porter stemming, as defined in the text.
(p. 92, steps 1a & 1b)
Run your system on p2-input-part-A.txt and output the term array to a file, with one term per line, after running your tokenizer, your stopword removal, and your stemmer, in that order.

Part B

Explore the most frequent terms in a longer document.

Tokenize The Count of Monte Cristo (UTF-8 text version) from Project Gutenberg (on moodle as p2-input-part-B.txt).
Use the same tokenizer, stopping approach, and stemmer as used in Part A.
Generate a list of the top 200 most frequent terms. Print out their text and their frequency.
Also note the requirements of item (6) in the report description below since you will need to collect some data as you tokenize for that.
