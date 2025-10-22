# ChessGame (Advanced Programming — 2024/2025)

A progressive Java implementation of the Chess game created as the practical assignment for the "Advanced Programming" course (2024/2025). The project uses object-oriented design, JavaFX for the graphical user interface, and follows recommended architectural patterns (MVVM / Facade) and testing practices.

---

## Table of Contents

- Project Overview
- Game Rules
- Key Features
- Architecture & Packages
- Requirements
- Development Workflow

---

## Project Overview

The ChessGame project provides a playable chess application developed in Java using the JavaFX UI toolkit. The implementation emphasizes:

- Clean separation between model and UI (no model-level user interaction),
- Use of a Facade pattern to expose game functionality,
- MVVM-like structure for UI updates (observable notifications),
- Unit tests (JUnit) for core model classes and the Facade,
- Persistence for saving/loading partial games.

The goal is to implement the standard chess rules and provide an intuitive graphical interface where two players can play, with extra support for undo/redo, accessibility features and an optional learning mode.

---

## Game Rules (reference)

Summary of standard moves implemented:
- King: one square any direction
- Queen: any number of squares any direction
- Rook: any number of squares horizontally or vertically
- Bishop: any number of squares diagonally
- Knight: L-shaped moves (2 + 1)
- Pawn: one square forward, two on first move; captures one square diagonally forward

Special rules to consider (may be implemented progressively):
- Castling
- En passant
- Pawn promotion
- Check and checkmate conditions
- Draw situations (stalemate, insufficient material, repetition, fifty-move rule, etc.)

The objective is to capture the opponent's King (game ends when King is captured per assignment), and moves that would leave one's own King in check should be prohibited.

---

## Key Features

Implemented features:

- Full chess movement rules for all piece types
- Capture mechanics and move validation
- Graphical board rendering using JavaFX, with images for each piece
- Turn management
- Persistence: save/load game state via serialization (import/export partial games)
- Undo/Redo functionality
- MVVM architecture with an observable Facade to update the UI
- Accessibility improvements (audio feedback)
- Learning mode to help new players

---

## Architecture & Packages

Project follows a package structure required by the course:

- pt.isec.pa.chess — base package containing application entry point
- pt.isec.pa.chess.model — Facade class exposing game functionality and coordinating model behavior
- pt.isec.pa.chess.model.data — domain model: Board, Piece base class and derived piece classes, Game state, Player, Moves
- pt.isec.pa.chess.ui — JavaFX UI classes and views
- pt.isec.pa.chess.ui.res — multimedia resources management (images, audio) using a multiton-like pattern

Separation rules:
- Model: no UI code, robust behavior and validation
- UI: no simulation logic, only presentation and user event pre-processing
- Facade: mediates between UI and model, observable to notify UI updates

---

## Requirements

- Java Development Kit (JDK) 11+ (JDK 17 recommended)
- JavaFX (version compatible with the chosen JDK; if using modular Java, ensure correct module setup)
- Build tool (recommended): Maven or Gradle (optional — IDE support also acceptable)
- JUnit 5 for unit testing

No external libraries beyond JavaFX and the standard testing libraries introduced in class should be used.

---

## Development Workflow

Per course rules and GitHub Classroom usage:

- Worked in a group with 2 more students.
- Created a new branch for each task/iteration.
- Submited a Pull Request and merge into `main` before each evaluation class session.

---

Acknowledgements
- Course: Advanced Programming 2024/2025
