# Score Card Full-Width Enhancement in Compact Mode

## Overview
Enhanced the sticky score card header to fill the full width of the screen when in compact mode (scrolled), removing the floating appearance and making it look more integrated with the UI.

## Changes Made

### 1. **LazyColumn Padding Removed**
**Before:**
```kotlin
LazyColumn(
    modifier = Modifier
        .padding(paddingValues)
        .padding(horizontal = 16.dp),  // ❌ Always had horizontal padding
    ...
)
```

**After:**
```kotlin
LazyColumn(
    modifier = Modifier
        .padding(paddingValues),  // ✅ No horizontal padding
    ...
)
```

### 2. **ScoreCardHeader Conditional Padding**
**Modifier logic:**
```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .then(
            if (isCompact) Modifier 
            else Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
        ),
    shape = if (isCompact) RoundedCornerShape(0.dp) else RoundedCornerShape(12.dp)
)
```

**Results:**
- **Compact mode**: No padding, no rounded corners → Edge-to-edge appearance
- **Normal mode**: 16dp horizontal padding, 16dp bottom padding, 12dp rounded corners

### 3. **Content Items Horizontal Padding Added**
Since the LazyColumn no longer has horizontal padding, added it to individual items:

**Instructions Text:**
```kotlin
Text(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 12.dp)
)
```

**Question Items:**
```kotlin
SummaryQuestionItem(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 8.dp)
)
```

**Empty State Card:**
```kotlin
Card(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(vertical = 32.dp)
)
```

## Visual Comparison

### Before (Floating Appearance):
```
┌──────────────────────────┐
│                          │ ← Screen Edge
│  ┌──────────────────┐   │
│  │   Score Card     │   │ ← 16dp padding on sides
│  │   (Compact)      │   │
│  └──────────────────┘   │
│                          │
│  ┌──────────────────┐   │
│  │  Question Item   │   │
│  └──────────────────┘   │
│                          │
└──────────────────────────┘
```

### After (Edge-to-Edge):
```
┌──────────────────────────┐
│                          │ ← Screen Edge
├──────────────────────────┤
│     Score Card (Compact) │ ← Full width, no gap
├──────────────────────────┤
│                          │
│  ┌──────────────────┐   │
│  │  Question Item   │   │ ← Still has padding
│  └──────────────────┘   │
│                          │
└──────────────────────────┘
```

## Benefits

✅ **Better Visual Integration**: Score card looks like a sticky toolbar when scrolled
✅ **More Screen Space**: Full width utilization for compact header
✅ **Professional Appearance**: No floating/disconnected look
✅ **Clear Hierarchy**: Header visually separates from content below
✅ **Material Design**: Follows sticky header patterns
✅ **Consistent Spacing**: Content items maintain proper padding

## Technical Details

### Modifier Chaining
Used `.then()` for conditional modifier application:
```kotlin
.then(
    if (isCompact) Modifier 
    else Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
)
```

This allows:
- Clean conditional logic
- No padding in compact mode
- Multiple padding modifiers chained in normal mode

### Shape Changes
- **Compact**: `RoundedCornerShape(0.dp)` → Sharp corners for edge-to-edge
- **Normal**: `RoundedCornerShape(12.dp)` → Rounded corners for card appearance

## User Experience

### When Not Scrolled (Normal Mode):
- Score card appears as a regular card
- Has padding and rounded corners
- Looks like a floating element
- Matches other content cards

### When Scrolled (Compact Mode):
- Score card expands to full width
- Sharp corners touch screen edges
- Looks like an integrated header bar
- Clear visual separation from content
- More space for stats display

## Performance
- No performance impact
- Uses existing animation system
- Efficient recomposition with `derivedStateOf`

## Files Modified
- `SummaryScreen.kt`: Updated LazyColumn and ScoreCardHeader layout logic

