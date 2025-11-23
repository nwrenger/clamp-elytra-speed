# Elytra Speed Cap

[![modrinth](https://img.shields.io/modrinth/v/elytra-speed-cap.svg)](https://modrinth.com/mod/elytra-speed-cap)
[![modrinth](https://img.shields.io/badge/dynamic/json?url=https://api.modrinth.com/v2/project/elytra-speed-cap&label=downloads&query=$.downloads&color=#00AF5C)](https://modrinth.com/mod/elytra-speed-cap)
[![modrinth](https://img.shields.io/modrinth/game-versions/elytra-speed-cap.svg)](https://modrinth.com/mod/elytra-speed-cap)

A lightweight mod that **limits the maximum speed of elytra flight** to a configurable value.

> Perfect for multiplayer servers that want to nerf elytra rushing, reduce chunk-loading lag, or simply keep travel speeds balanced.

## How It Works

Minecraft calculates elytra movement primarily on the server, while the client **predicts** movement locally to keep the flight smooth.

To enforce a maximum speed:

- **Server side**:
  Applies the speed limit and caps the velocity of players who exceed it.
- **Client side (optional but recommended)**:
  Adjusts local prediction to match the server's configured maximum for smoother behavior.
  Without the client mod, players who exceed the limit may experience rubberbanding.

**Server owners should encourage players to install the mod locally** for the best experience.

## Configuration

A config file is created at:

```sh
./config/elytra-speed-cap.json
```

Default structure:

```json
{
  "max_speed": 60.0
}
```

- `max_speed`: Maximum allowed elytra speed in **blocks/second**.

After changing the value, **restart the server** to apply it.

## Contributing & Issues

I warmly welcome:

- Bug reports
- Feature requests
- Pull requests

Please open issues or PRs on [GitHub](https://github.com/nwrenger/elytra-speed-cap/issues).

## License

This project is licensed under the **MIT License**. See [LICENSE](https://github.com/nwrenger/elytra-speed-cap/blob/main/LICENSE) for details.
