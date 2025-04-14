# Real-Time Exchange Rate Tracker (test task)

An Android application that displays real-time exchange rates for selected currencies and crypto assets. Users can add or remove assets to track, with live updates every 5 seconds.

## Features

- **Home Screen** showing selected currencies/assets and their current exchange rates.
- **Search & Add Assets** from an API .
- **Remove Assets** from the watchlist with a SWIPE.
- Cached last known rates available **offline**.
- Automatic refresh of prices every 5 seconds.

## Tech Stack

- **Jetpack Compose** – modern declarative UI framework
- **Ktor** – lightweight HTTP client for API interactions
- **Koin** – dependency injection framework
- **MVI Architecture** – for clean and testable code design
- **CoinCap API** – [https://pro.coincap.io/api-docs/](https://pro.coincap.io/api-docs/)

## Getting Started

Clone the repository:

```bash
git clone https://github.com/copernics/Al-Hilal-Test.git
cd Al-Hilal-Test
