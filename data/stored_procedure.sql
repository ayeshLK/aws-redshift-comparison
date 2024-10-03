CREATE OR REPLACE PROCEDURE create_temp_user_data()
AS $$
BEGIN
    -- Create a temporary table if it does not already exist
    EXECUTE 'CREATE TEMP TABLE IF NOT EXISTS temp_user_data (
        userid INT,
        username VARCHAR(255),
        email VARCHAR(255),
        phone VARCHAR(20)
    )';

    -- Insert data into the temporary table
    EXECUTE 'INSERT INTO temp_user_data (userid, username, email, phone)
             SELECT userid, username, email, phone
             FROM users';
END;
$$ LANGUAGE plpgsql;
