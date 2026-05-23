# Ad Auction Dashboard

A JavaFX desktop application for loading advertising campaign logs, calculating campaign performance metrics, and visualising those metrics as charts. The app was built for an ad auction analytics scenario where a user can inspect impressions, clicks, costs, conversions, bounce behaviour, and audience segments.

## Features

- Login system with default user and admin accounts
- Admin-only user management with password updates, deletion, listing, and MFA setup for newly registered users
- CSV import for impression, click, and server interaction logs
- Main chart view for campaign metrics over daily, weekly, or monthly time periods
- Filters for gender, age, income, and context
- Metrics for impressions, clicks, unique visitors, bounces, conversions, total cost, CTR, CPA, CPC, CPM, and bounce rate
- Dual-chart comparison view
- Click cost histogram view
- Overall campaign metrics page
- Configurable bounce definition
- Light and dark mode
- Built-in help slideshow
- PDF export for charts

## Requirements

- Java 17 or newer
- Apache Maven 3.9+
- Windows, macOS, or Linux with a desktop environment capable of running JavaFX

The project is configured for Java 17 in `pom.xml`. Newer JDKs may work, but Java 17 is the safest option if JavaFX gives startup errors.

## Quick Start

Clone the repository and enter the project folder:

```powershell
git clone https://github.com/joejewell99/Ad-Auction-Dashboard.git
cd Ad-Auction-Dashboard
```

Run the application:

```powershell
mvn javafx:run
```

The main class is:

```text
com.example.App
```

The application starts in fullscreen mode on the login page.

## First-Time Use

1. Log in with the default user account:

```text
Username: user
Password: pass
```

2. On the file input screen, select the bundled CSV files from:

```text
src/main/resources
```

3. Select these files when prompted:

```text
impression_log.csv
clicks_log.csv
server_log.csv
```

4. Click **Proceed** to open the main chart dashboard.

The main dashboard lets you switch metrics, change the time granularity, filter by audience attributes, compare charts, view overall metrics, export charts to PDF, and inspect the click-cost histogram.

## Login Details

Default standard user:

```text
Username: user
Password: pass
```

Default administrator:

```text
Username: admin
Password: adminPass
```

The admin account can be used from the main login screen or through **Manage Users**.

Newly registered users are given an MFA secret and must verify login with a 6-digit authenticator code. The built-in `user` and `admin` accounts bypass MFA.

## Input CSV Files

The app expects three CSV files with exact filenames:

```text
impression_log.csv
clicks_log.csv
server_log.csv
```

The file chooser validates the names, so renamed files will be rejected even if the contents are valid.

Sample/full data files are already included in:

```text
src/main/resources/impression_log.csv
src/main/resources/clicks_log.csv
src/main/resources/server_log.csv
```

When the app asks for the logs, select those files from `src/main/resources`.

### Impression Log Format

Filename:

```text
impression_log.csv
```

Columns:

```text
Date,ID,Gender,Age,Income,Context,Impression Cost
```

Example:

```csv
Date,ID,Gender,Age,Income,Context,Impression Cost
2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,0.001713
```

Accepted values used by the app include:

- Gender: `Male`, `Female`
- Age: `<25`, `25-34`, `35-44`, `45-54`, `>54`
- Income: `Low`, `Medium`, `High`
- Context: `News`, `Shopping`, `Social Media`, `Blog`, `Hobby`, `Travel`

### Click Log Format

Filename:

```text
clicks_log.csv
```

Columns:

```text
Date,ID,Click Cost
```

Example:

```csv
Date,ID,Click Cost
2015-01-01 12:01:21,8895519749317550080,11.794442
```

### Server Log Format

Filename:

```text
server_log.csv
```

Columns:

```text
Entry Date,ID,Exit Date,Pages Viewed,Conversion
```

Example:

```csv
Entry Date,ID,Exit Date,Pages Viewed,Conversion
2015-01-01 12:01:21,8895519749317550080,2015-01-01 12:05:13,7,No
```

Use `Yes` or `No` in the `Conversion` column.

## If the CSV Files Are Missing

First check whether Git still has them:

```powershell
git status --short
git ls-files src/main/resources/*_log.csv
```

If the files are tracked but missing from the working tree, restore them with:

```powershell
git restore src/main/resources/impression_log.csv src/main/resources/clicks_log.csv src/main/resources/server_log.csv
```

If you need to create replacement test data manually, keep the filenames and column headers exactly as shown above. The IDs in `clicks_log.csv` and `server_log.csv` should correspond to IDs from `impression_log.csv` so filtering and metric calculations remain meaningful.

## Running Tests

```powershell
mvn test
```

The test suite covers chart calculations, filtering, login/database behaviour, and PDF export.

## Building a Jar

```powershell
mvn package
```

The build uses the Maven Shade Plugin and creates a shaded jar in `target`.

An older packaged artifact is also present at:

```text
out/artifacts/ad_auction_dashboard_jar/ad-auction-dashboard.jar
```

For current development, prefer running through Maven.

## Project Structure

```text
src/main/java/com/example
```

Main application code.

```text
src/main/resources
```

Help images and bundled campaign CSV logs.

```text
src/test/java
```

JUnit tests.

Important classes:

- `App` - JavaFX application entry point
- `Login` - main login screen
- `LoginDatabase` - SQLite-backed user database and default account setup
- `InputFilesPage` - CSV file selection
- `LogManager` - CSV parsing and in-memory log storage
- `ChartCreator` - metric calculations and chart generation
- `ChartPage` - main chart screen
- `MultiChartPage` - two-chart comparison screen
- `HistogramChart` - click cost distribution view
- `OverallMetricsPage` - campaign summary metrics
- `UserManagementPage` - admin-only account management

## Notes and Troubleshooting

- The app creates or updates `login.db` in the project root when it starts.
- If default credentials stop working, the local `login.db` may have been changed. Deleting it will allow the app to recreate the default accounts on the next run, but this also removes registered users.
- The app uses simple comma splitting for CSV parsing, so avoid commas inside field values.
- The input page requires exact filenames, not just matching headers.
- If JavaFX fails to launch, use JDK 17 and rerun `mvn javafx:run`.
