## Login Screen UI

This section covers building the login screen's UI. In Android, UIs were written alsmot exclusively in XML directly or by using a visual editor. However, the newest and definetly the standard way moving forward is to use Jetpack Compose. Jetpack Compose is a declarative way to write UI codes using Kotlin. Besides being simpler, it has many other advantages you can read about [here](https://developer.android.com/jetpack/compose). Or if you want to dig deeper into the subjet, Google also offers [a pathway program](https://developer.android.com/courses/pathways/compose).

### Composable functions

Jetpack Compose is built around composable functions. These functions let you define your app's UI programmatically by describing its shape and data dependencies. To create a composable function, just add the `@Composable` annotation to the function name, like so:
```kotlin
@Composable
fun LoginEditText() { ... }
```

Keep in mind, composable functions can only be called within the scope of other composable functions.

And from within these composable functions, you can use one or more of other custom or default composable UI components to start composing your views. For example, if you wanted to create a row of 3 texts and a button at the bottom, we can do so like this:
```kotlin
@Composable
fun ThreeTextAndOneButton() {
    Column(horizontalAlignment = CenterHorizontally) {
        // Text is also a composable function.
        Text("Text 1")
        Text("Text 2")
        Text("Text 3")
        // So is Button.
        Button(onClick = { }) {
            Text("A Button Text")
        }
    }
}
```

If you were to run the app with above composable as its UI, you'd see something like this:
<img src="login-ui-column.png" width="400">

Notice how the text and other UI elements are all aligned to the left edge of the screen?
That is the default behavior, and we can change it using something called `modifier`. 

`modifier` lets you decorate or add behavior to Compose UI elements. For example, backgrounds, padding and click event listeners decorate or add behavior to rows, text or buttons.

Let's try and add some left padding to the child views inside `Column` using `modifier`:
```kotlin
@Composable
fun ThreeTextAndOneButton() {
    Column(modifier = Modifier.padding(start = 12.dp)) {
        Text("Text 1")
        Text("Text 2")
        Text("Text 3")
        // So is Button.
        Button(onClick = { }) {
            Text("A Button Text")
        }
    }
}
```
<img src="login-ui-column-leftpadding.png" width="400">

Nice! Now there are some breathing room.

> *What is dp in 12.dp here?* dp is a density-independent Pixels - an abstract unit that is based on the physical density of the screen. These units are relative to a 160 dpi screen, so one dp is one pixel on a 160 dpi screen. The ratio of dp-to-pixel will change with the screen density, but not necessarily in direct proportion. This is the standard measurement unit for UIs in android development to support multiple devices with different screen sizes.

I hope you are starting to get a hang of how jetpack compose works! This ia a topic that goes deep and wide. So I won't go over every detail here, but as we progress further, we will discover more and more of its awesome features.

### Divide and conqure

Nobody likes duplicated code, as it increases upkeep cost and redundant development time. Luckily, compose is all about making UIs more modular and reusable.

First, let's start by taking a look at what we are trying to create:

<img src="login-ui-div.png" width="400">

We can see that roughly, there are 4 types of UI components we would need to create. Some, we can create just using what comes standard with Jetpack Compose library. Others, we need to create ourselves.

1. Text component to hold our bank's name
1. Text input component to accept user name and password
1. Check box component to let users decide if they want their username rememebred
1. And finally the sign in button itself

Let's start by creating what lloks the easiest, the text component.

### Text component

I don't think you are suprised to find that something as essential as text component comes standard with the library. And to use it, all we need to do is this:

* Go to `LoginFragment.kt`, and inside `setContent {...}` block, add the following:
```kotlin
/**
*  setContent set the Jetpack Compose UI content for this view.
*/
setContent {
    Text(
        text = "Summit Bank"
    )
}
```
The `setContent` block defines the activity's layout. Instead of defining the layout contents with the traditional XML file, we call composable functions. Jetpack Compose library then transform these composable functions into the app's UI elements. Thus overcomming the chicken or the egg problem with composable functions needing to be called within another composable function.


If you run the app now, you will see something like this:


<img src="login-ui-summitbank1.png" width="400">


Text is correct, but the design in not quite, what we want.

Let's think what we are missing. First of all, we want all of our components to be inside a `Column` since they need to be stacked vertically. Also, we would want to use modifiers to decorate the components.

Now with that in mind, let's try something like this:
```kotlin
/**
 * Set the Jetpack Compose UI content for this view.
 */
setContent {
    /**
     * A layout composable that places its children in a vertical sequence.
     */
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Summit Bank",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(vertical = 32.dp)
        )
    }
}
```

We now wrapped the `Text` component inside a `Column` colmponent. We also used modifier and other component parameters to decorate them. Here is what each of them do:

**Column**
* `horizontalAlignment` parameter to defin child component alignment. Since we want our views to be centered, we chose `Alignment.CenterHorizontally`.
* `Modifier.fillMaxWidth()` a modifier that tells column to take up the entire width of the screen.

**Text**
* `text` text value we want to show.
* `style` parameter that decides what type of text style we want to use. You can see all the options [here](https://material.io/design/typography/the-type-system.html#type-scale)
* `fontWeight` the weight we want our font to have. We want a very bold look, so we went with `Black` here.
* `Modifier.padding(vertical = 32.dp)` we add 32.dp of padding to both top and bottom of our Text component so others views we add later won't be touching.

Let's run and see what we made!


<img src="login-ui-title.png" width="400">

Nice! We are off to a good start. Now let's tackle a bit more complicated text input components.

### OutlinedTextField

Lucky us! This component is also pre-built withint the library. We can add it to our view by appending it to the end of Text component like so:

Our login screen's UI will go into `app/src/main/res/layout/activity_login.xml` (the file has already been created). In general, all **res**ources
for an Android app goes under `app/src/main/res/`, this includes XML layout files, but also things like images, colors, and sound files.

<img src="login-initial.png" width="800">

If they are not showing, you can enable the previews for the bottom bar and the top app bar by clicking the eye icon and enabling `Layout Decorations`.
- **In newer versions of Android Studio**, this option has been renamed to `Show System UI`.

<img src="login-layout-decorations.png" width="400">



### Learning ConstriantLayout
We're going to use a [ConstraintLayout](https://developer.android.com/training/constraint-layout) to design our login screen.
A `ConstraintLayout` works by defining relationships (constraints) between items on the screen (in
this case, a title label, two input text fields, a button, and a loading spinner). There are other layouts which exist,
like the [LinearLayout](https://developer.android.com/guide/topics/ui/layout/linear) which simply places components in a list either horizontally or vertically (and you can nest `LinearLayout`s
inside of each other to create more complex UIs). The `ConstraintLayout` requires a little bit of a learning curve,
but can be used to create more-complex UIs with ease if used correctly.

As shown in the `Component Tree` panel, our UI _already_ has a `ConstraintLayout` which contains the
single, centered text label (called a `TextView`).

Let's see this in action by first **deleting** the existing `TextView`, by selecting it and hitting Delete / Backspace.

Then, we'll place our own `TextView` by dragging one off the palette and dropping it in the canvas.

<img src="login-create-textview.gif" width="800">

Select the new `TextView`. When using a `ConstraintLayout`, any of its child `View`s will get four draggable handles (anchors)
used to constrain each of the four sides.

Each `View` in a `ConstraintLayout` is **required** to have at least 1 horizontal and 1 vertical constraint,
otherwise, it will default to snapping to the top-left corner, which you can see by running the app, since we have not set any constraints.

(the Layout Editor may not show it snapped to the top-left corner, but this is just for convenience during UI design)

<img src="login-no-constraints.png" width="200">

You can click-and-drag the anchors to align it to the sides of the screen, or even to other `View`s once we
have more than one. We can see this by playing in the layout editor.

<img src="login-constraints.gif" width="400">

If you set **opposing constraints** (e.g. left and right, or top and bottom), the `View` will center itself between the two.

<img src="login-constraints-centering.gif" width="400">

We would like this `TextView` to be centered at the top of our screen, to become a title later, so we only
need to set the **top**, **left**, and **right** constraint.

The bottom constraint is not needed: to delete a constraint, Cmd+Click (or Ctrl+Click) on the anchor. Deletion is also an option if
you right-click on the anchor.

<img src="login-delete-constraint.gif" width="400">

### Setting Attributes

With our `TextView` in the right position, we can set some of its attributes in the right panel:
- Set the `id` to `title`
- Use the `Constraint Widget` to set a top margin of `8`
- Set the `text` to `Welcome to Summit!`
- Set the `textSize` to `18sp`
- Click the bold `B` under `textStyle`

<img src="login-title-attributes.gif" width="600">

<img src="login-title-attributes2.gif" width="600">

The `id` is like a "variable name" for the UI element. It has no visual impact, but will be needed later to reference this `View` from code.

Units-of-measurement in Android are not necessarily pixels. Smartphones come in different sizes and _pixel densities_ (how many pixels
comprise the screen). e.g. two Android phones may have the same screen size (say, in inches), but have different pixel densities. So, it's important
to not use pixels for measurement, as your UI would end up looking differently on different screens.

Android provides two primary units-of-measurement that address this concern:
  - _Density-independent pixels_ (dp). Android will figure out how much to scale this value by depending on the screen size (so it looks the same on all devices).  e.g. This might be 8 _pixels_ on a smaller screen vs. 16 _pixels_ on a larger screen.
    - For example, our title's top margin is set using dp.
  - _Scalable pixels_ (sp). This is similar to the (dp), except it also considers the user's system-wide font size (e.g. a user may prefer larger text).
    - For example, our title's text size is set using sp.

[Official Docs - Support Different Screen Sizes](https://developer.android.com/training/multiscreen/screendensities)

### Placing the Username Field
In the palette, go to the `Text` section and drag-and-drop a `Plain Text` component onto the canvas. This
is an `EditText` view and is a text field that the user can type into.

We want it to be centered, but _below_ our title view. The left & right constraints will be set to the sides
of the device. The top constraint will be linked to the **bottom** anchor of the title view.

<img src="login-username-initial.gif" width="600">

<img src="login-username-constraints.gif" width="600">

For attributes:
- Set the `id` to `username`
- Use the `Constraint Widget` to set a top margin of `16` and left & right margins of `8`
- Set the `text` to `Username`

<img src="login-username-attributes.gif" width="600">

Run the app. Notice how the user has to delete the word "Username" in order to type their name, which is inconvenient.

<img src="login-username-inconvenient.gif" width="350">

Instead of setting the `text` attribute, set the `hint` attribute, which sets the text on the background
of the `EditText`, until the user types anything.

<img src="login-username-hint.png" width="350">

<img src="login-username-hint.gif" width="350">

### Placing the Password Field
In the palette, drag a `Password` component under the `Text` section.

Constrain it under the username field, similar to how the username was done, except constrain the top
side to the bottom anchor of the username field.

For attributes:
- Set the `id` to `password`
- Use the `Constraint Widget` to set top, left, and right sides to `8`
- Set the `hint` to `Password`

### Viewing the XML Representation
For everything we've been doing in the Layout Editor, XML code is generated underneath.

You can view it by clicking the Code button above the Attributes panel. You can also click the Design
button in this area to go back.

<img src="login-xml-button.png" width="350">

We can see that the XML representation is roughly understandable, based on what we did in the
Layout Editor. It's possible to design your layout fully by writing this XML code directly and this is
up to personal preference. I generally use both the Layout Editor and XML directly -- for example,
some UI attributes are easier to just type out in XML rather than finding them in the Attributes panel.

<img src="login-xml-example.png" width="800">

Some of the lines are highlighted yellow as warnings, we'll address these in a later section.

Make sure you click the Design button to go back to the Layout Editor.

<img src="login-xml-button2.png" width="350">

### Placing the Sign In Button
We'll place a `Button` component next (under `Buttons` in the palette). We'll set the
constraints similar to how we did the last two (but below the password field).

For attributes:
- Set the `id` to `sign_in`
- Use the `Constraint Widget` to set top to `8` and the left & right sides to `16`
- Set the `text` to `Sign In`

To get the width of the bottom to expand to fill the available space, choose `0dp (match_constraint)` for the `layout_width`.
The button will expand to fill the available space, minus the margins.

<img src="login-button-0dp.png" width="350">

### Placing the Progress Bar
Finally, we'll place a `ProgressBar` (under `Widgets` in the palette) and constrain it below the button.

For attributes:
- We can leave the `id` alone, the default `progressBar` is adequate
- Use the `Constraint Widget` to set top to `8`

We want the progress bar to be initially invisible, to do this set the `visibility` attribute to `invisible`

There's also `gone` as an option. The difference between `invisible` and `gone` is whether or not the view's
bounding box will still be kept. This might be relevant if other items are being constrained to your `ProgressBar`
and you do (or don't) want them to be shifted accordingly.

<img src="login-progressbar-visibility.gif" width="600">

### Initial UI Complete!
This completes our initial UI for the login screen!

There are a few improvements we'll come back and add later -- adding a "Remember Me"
switch and addressing some of the XML warnings.

<img src="login-initial-final.png" width="350">

Here's the final XML code for this screen. If you've fallen behind or missed something, you
can copy paste this into the Code section of the `activity_login.xml` screeen.

<img src="login-xml-button.png" width="350">

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Welcome to Summit!"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/username"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="8dp"
        android:ems="10"
        android:hint="Username"
        android:inputType="textPersonName"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/title" />

    <EditText
        android:id="@+id/password"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="8dp"
        android:ems="10"
        android:hint="Password"
        android:inputType="textPassword"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/username" />

    <Button
        android:id="@+id/sign_in"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="16dp"
        android:text="Sign In"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/password" />

    <ProgressBar
        android:id="@+id/progressBar"
        style="?android:attr/progressBarStyle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:visibility="invisible"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/sign_in" />
    
</androidx.constraintlayout.widget.ConstraintLayout>
```

[Back to Index](../README.md)
